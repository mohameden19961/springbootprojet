package supnum.projet.Library.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import supnum.projet.Library.data.entities.*;
import supnum.projet.Library.data.repositories.*;
import supnum.projet.Library.data.enums.*;
import supnum.projet.Library.dto.*;
import supnum.projet.Library.exceptions.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
@Transactional
public class BorrowService {
    
    private final BorrowRepository borrowRepository;
    private final BookItemRepository bookItemRepository;
    private final MemberRepository memberRepository;
    private final ReservationRepository reservationRepository;
    private final ReservationService reservationService;
    
    private static final int BORROW_DURATION_DAYS = 30;
    private static final int MAX_RENEWAL_COUNT = 3;
    
    /**
     * Emprunter un exemplaire de livre
     * Règles métier à vérifier :
     * - L'exemplaire doit être disponible
     * - Le membre ne doit pas avoir dépassé maxBorrows
     * - Il ne doit pas y avoir une réservation prioritaire
     */
    public BorrowDTO borrowBook(Long memberId, Long bookItemId) {
        Member member = memberRepository.findById(memberId)
                .filter(m -> !m.getDeleted())
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with id: " + memberId));
        
        BookItem bookItem = bookItemRepository.findById(bookItemId)
                .filter(bi -> !bi.getDeleted())
                .orElseThrow(() -> new ResourceNotFoundException("Book item not found with id: " + bookItemId));
        
        // Vérifier que l'exemplaire est disponible
        if (bookItem.getStatus() != BookItemStatus.AVAILABLE) {
            throw new BusinessException("Book item is not available for borrowing. Current status: " + bookItem.getStatus());
        }
        
        // Vérifier le nombre d'emprunts actifs du membre
        long activeBorrows = borrowRepository.countActiveBorrowsByMemberId(memberId);
        if (activeBorrows >= member.getMaxBorrows()) {
            throw new BusinessException("Member has reached maximum number of active borrows: " + member.getMaxBorrows());
        }
        
        // Vérifier s'il existe une réservation prioritaire
        var firstReservation = reservationRepository.findFirstPendingReservation(bookItem.getBook().getId());
        if (firstReservation.isPresent() && !firstReservation.get().getMember().getId().equals(memberId)) {
            throw new BusinessException("Book has a priority reservation from another member");
        }
        
        // Créer l'emprunt avec optimistic locking
        Borrow borrow = Borrow.builder()
                .member(member)
                .bookItem(bookItem)
                .status(BorrowStatus.ACTIVE)
                .renewalCount(0)
                .borrowedAt(LocalDateTime.now())
                .dueDate(LocalDateTime.now().plusDays(BORROW_DURATION_DAYS))
                .deleted(false)
                .build();
        
        // Mettre à jour le statut de l'exemplaire
        bookItem.setStatus(BookItemStatus.BORROWED);
        bookItemRepository.save(bookItem);
        
        // Annuler la réservation si elle existe
        if (firstReservation.isPresent()) {
            var reservation = firstReservation.get();
            reservation.setStatus(ReservationStatus.COMPLETED);
            reservationRepository.save(reservation);
        }
        
        Borrow saved = borrowRepository.save(borrow);
        return mapToDTO(saved);
    }
    
    /**
     * Retourner un exemplaire emprunté
     * Règles métier :
     * - Vérifier l'existence d'une réservation en attente
     * - Mettre à jour le statut du BookItem
     * - Mettre à jour le statut de l'emprunt
     */
    public BorrowDTO returnBook(Long borrowId) {
        Borrow borrow = borrowRepository.findById(borrowId)
                .filter(b -> !b.getDeleted())
                .orElseThrow(() -> new ResourceNotFoundException("Borrow not found with id: " + borrowId));
        
        if (borrow.getStatus() != BorrowStatus.ACTIVE && borrow.getStatus() != BorrowStatus.OVERDUE) {
            throw new BusinessException("Cannot return book with status: " + borrow.getStatus());
        }
        
        BookItem bookItem = borrow.getBookItem();
        
        // Mettre à jour l'emprunt
        borrow.setStatus(BorrowStatus.RETURNED);
        borrow.setReturnedAt(LocalDateTime.now());
        
        // Vérifier s'il y a une réservation en attente
        var firstReservation = reservationRepository.findFirstPendingReservation(bookItem.getBook().getId());
        
        if (firstReservation.isPresent()) {
            // Le livre devient réservé
            bookItem.setStatus(BookItemStatus.RESERVED);
            var reservation = firstReservation.get();
            reservation.setStatus(ReservationStatus.READY);
            reservationRepository.save(reservation);
        } else {
            // Le livre redevient disponible
            bookItem.setStatus(BookItemStatus.AVAILABLE);
        }
        
        bookItemRepository.save(bookItem);
        Borrow updated = borrowRepository.save(borrow);
        return mapToDTO(updated);
    }
    
    /**
     * Renouveler un emprunt
     * Règles métier :
     * - Maximum 3 renouvellements
     * - Pas de renouvellement si une réservation existe
     */
    public BorrowDTO renewBorrow(Long borrowId) {
        Borrow borrow = borrowRepository.findById(borrowId)
                .filter(b -> !b.getDeleted())
                .orElseThrow(() -> new ResourceNotFoundException("Borrow not found with id: " + borrowId));
        
        if (borrow.getStatus() != BorrowStatus.ACTIVE) {
            throw new BusinessException("Cannot renew borrow with status: " + borrow.getStatus());
        }
        
        // Vérifier le nombre de renouvellements
        if (borrow.getRenewalCount() >= MAX_RENEWAL_COUNT) {
            throw new BusinessException("Maximum renewal count (" + MAX_RENEWAL_COUNT + ") reached");
        }
        
        // Vérifier s'il existe une réservation
        var existingReservation = reservationRepository.findFirstPendingReservation(borrow.getBookItem().getBook().getId());
        if (existingReservation.isPresent()) {
            throw new BusinessException("Cannot renew borrow as there is an active reservation");
        }
        
        // Renouveler
        borrow.setRenewalCount(borrow.getRenewalCount() + 1);
        borrow.setDueDate(LocalDateTime.now().plusDays(BORROW_DURATION_DAYS));
        
        Borrow updated = borrowRepository.save(borrow);
        return mapToDTO(updated);
    }
    
    @Transactional(readOnly = true)
    public Page<BorrowDTO> getBorrowsByMember(Long memberId, Pageable pageable) {
        // Vérifier que le membre existe
        memberRepository.findById(memberId)
                .filter(m -> !m.getDeleted())
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with id: " + memberId));
        
        return borrowRepository.findByMemberIdActive(memberId, pageable)
                .map(this::mapToDTO);
    }
    
    @Transactional(readOnly = true)
    public BorrowDTO getBorrowById(Long id) {
        Borrow borrow = borrowRepository.findById(id)
                .filter(b -> !b.getDeleted())
                .orElseThrow(() -> new ResourceNotFoundException("Borrow not found with id: " + id));
        return mapToDTO(borrow);
    }
    
    @Transactional(readOnly = true)
    public Page<BorrowDTO> getBorrowsByStatus(BorrowStatus status, Pageable pageable) {
        return borrowRepository.findByStatusActive(status, pageable)
                .map(this::mapToDTO);
    }
    
    public void deleteBorrow(Long id) {
        Borrow borrow = borrowRepository.findById(id)
                .filter(b -> !b.getDeleted())
                .orElseThrow(() -> new ResourceNotFoundException("Borrow not found with id: " + id));
        
        borrow.setDeleted(true);
        borrowRepository.save(borrow);
    }
    
    private BorrowDTO mapToDTO(Borrow borrow) {
        return BorrowDTO.builder()
                .id(borrow.getId())
                .memberId(borrow.getMember().getId())
                .bookItemId(borrow.getBookItem().getId())
                .renewalCount(borrow.getRenewalCount())
                .status(borrow.getStatus())
                .borrowedAt(borrow.getBorrowedAt())
                .returnedAt(borrow.getReturnedAt())
                .dueDate(borrow.getDueDate())
                .build();
    }
}
