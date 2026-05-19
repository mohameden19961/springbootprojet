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

@Service
@RequiredArgsConstructor
@Transactional
public class ReservationService {
    
    private final ReservationRepository reservationRepository;
    private final MemberRepository memberRepository;
    private final BookRepository bookRepository;
    private final BookItemRepository bookItemRepository;
    
    /**
     * Réserver un livre
     * Le livre doit avoir au moins un exemplaire non disponible
     * Gère automatiquement la position en file d'attente
     */
    public ReservationDTO createReservation(Long memberId, Long bookId) {
        Member member = memberRepository.findById(memberId)
                .filter(m -> !m.getDeleted())
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with id: " + memberId));
        
        Book book = bookRepository.findById(bookId)
                .filter(b -> !b.getDeleted())
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + bookId));
        
        // Vérifier s'il y a un exemplaire disponible
        if (bookItemRepository.countAvailableByBookId(bookId) > 0) {
            throw new BusinessException("Cannot create reservation: book has available copies");
        }
        
        // Vérifier si une réservation active existe déjà pour ce membre et ce livre
        var existingReservation = reservationRepository.findPendingReservation(memberId, bookId);
        if (existingReservation.isPresent()) {
            throw new BusinessException("Member already has an active reservation for this book");
        }
        
        // Déterminer la position en file d'attente
        var lastReservation = reservationRepository.findByBookIdActive(bookId, Pageable.ofSize(1))
                .stream()
                .findFirst();
        
        Integer queuePosition = lastReservation.isPresent() ? 
                lastReservation.get().getQueuePosition() + 1 : 1;
        
        Reservation reservation = Reservation.builder()
                .member(member)
                .book(book)
                .queuePosition(queuePosition)
                .status(ReservationStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .deleted(false)
                .build();
        
        Reservation saved = reservationRepository.save(reservation);
        return mapToDTO(saved);
    }
    
    @Transactional(readOnly = true)
    public Page<ReservationDTO> getReservationsByMember(Long memberId, Pageable pageable) {
        // Vérifier que le membre existe
        memberRepository.findById(memberId)
                .filter(m -> !m.getDeleted())
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with id: " + memberId));
        
        return reservationRepository.findByMemberIdActive(memberId, pageable)
                .map(this::mapToDTO);
    }
    
    @Transactional(readOnly = true)
    public Page<ReservationDTO> getReservationQueueForBook(Long bookId, Pageable pageable) {
        // Vérifier que le livre existe
        bookRepository.findById(bookId)
                .filter(b -> !b.getDeleted())
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + bookId));
        
        return reservationRepository.findByBookIdActive(bookId, pageable)
                .map(this::mapToDTO);
    }
    
    @Transactional(readOnly = true)
    public ReservationDTO getReservationById(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .filter(r -> !r.getDeleted())
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found with id: " + id));
        return mapToDTO(reservation);
    }
    
    @Transactional(readOnly = true)
    public Page<ReservationDTO> getReservationsByStatus(ReservationStatus status, Pageable pageable) {
        return reservationRepository.findByStatusActive(status, pageable)
                .map(this::mapToDTO);
    }
    
    /**
     * Annuler une réservation
     */
    public void cancelReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .filter(r -> !r.getDeleted())
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found with id: " + id));
        
        if (reservation.getStatus() == ReservationStatus.COMPLETED || 
            reservation.getStatus() == ReservationStatus.CANCELLED) {
            throw new BusinessException("Cannot cancel reservation with status: " + reservation.getStatus());
        }
        
        reservation.setStatus(ReservationStatus.CANCELLED);
        
        // Réorganiser la file d'attente
        reorganizeQueue(reservation.getBook().getId());
        
        reservationRepository.save(reservation);
    }
    
    /**
     * Supprimer logiquement une réservation
     */
    public void deleteReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .filter(r -> !r.getDeleted())
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found with id: " + id));
        
        reservation.setDeleted(true);
        reservationRepository.save(reservation);
    }
    
    /**
     * Réorganiser la file d'attente d'un livre
     */
    @Transactional
    public void reorganizeQueue(Long bookId) {
        var reservations = reservationRepository.findByBookIdActive(bookId, Pageable.ofSize(1000))
                .getContent();
        
        int position = 1;
        for (Reservation reservation : reservations) {
            if (reservation.getStatus() == ReservationStatus.PENDING) {
                reservation.setQueuePosition(position);
                position++;
                reservationRepository.save(reservation);
            }
        }
    }
    
    private ReservationDTO mapToDTO(Reservation reservation) {
        return ReservationDTO.builder()
                .id(reservation.getId())
                .memberId(reservation.getMember().getId())
                .bookId(reservation.getBook().getId())
                .queuePosition(reservation.getQueuePosition())
                .status(reservation.getStatus())
                .createdAt(reservation.getCreatedAt())
                .build();
    }
}
