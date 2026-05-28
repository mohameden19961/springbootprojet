package supnum.projet.Library.services;

import supnum.projet.Library.data.entities.*;
import supnum.projet.Library.data.entities.enums.*;
import supnum.projet.Library.data.repositories.*;
import supnum.projet.Library.dto.response.BorrowResponse;
import supnum.projet.Library.exceptions.BusinessException;
import supnum.projet.Library.exceptions.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class BorrowService {

    private final BorrowRepository borrowRepository;
    private final MemberRepository memberRepository;
    private final BookItemRepository bookItemRepository;
    private final ReservationRepository reservationRepository;

    public BorrowService(BorrowRepository borrowRepo, MemberRepository memberRepo,
                         BookItemRepository bookItemRepo, ReservationRepository reservationRepo) {
        this.borrowRepository = borrowRepo;
        this.memberRepository = memberRepo;
        this.bookItemRepository = bookItemRepo;
        this.reservationRepository = reservationRepo;
    }

    public BorrowResponse borrowBook(Long memberId, String barcode) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new ResourceNotFoundException("Membre introuvable avec l'id : " + memberId));

        BookItem item = bookItemRepository.findByBarcode(barcode)
            .orElseThrow(() -> new ResourceNotFoundException("Exemplaire introuvable avec le code-barres : " + barcode));

        if (item.getStatus() != BookItemStatus.AVAILABLE) {
            throw new BusinessException("L'exemplaire n'est pas disponible");
        }

        long activeBorrows = borrowRepository.countByMemberAndStatus(member, BorrowStatus.ACTIVE);
        if (activeBorrows >= member.getMaxBorrows()) {
            throw new BusinessException("Le membre a atteint sa limite maximale d'emprunts");
        }

        item.setStatus(BookItemStatus.BORROWED);
        bookItemRepository.save(item);

        Borrow borrow = Borrow.builder()
            .member(member)
            .bookItem(item)
            .status(BorrowStatus.ACTIVE)
            .renewalCount(0)
            .borrowDate(LocalDateTime.now())
            .dueDate(LocalDate.now().plusDays(14))
            .build();

        Book book = item.getBook();
        if (book != null) {
            List<Reservation> pending = reservationRepository
                .findByMemberAndBookAndStatus(member, book, ReservationStatus.PENDING);
            pending.forEach(r -> r.setStatus(ReservationStatus.CANCELLED));
            reservationRepository.saveAll(pending);
        }

        return toResponse(borrowRepository.save(borrow));
    }

    public BorrowResponse returnBook(Long borrowId) {
        Borrow borrow = borrowRepository.findById(borrowId)
            .orElseThrow(() -> new ResourceNotFoundException("Emprunt non trouvé avec l'id : " + borrowId));

        if (borrow.getStatus() != BorrowStatus.ACTIVE) {
            throw new BusinessException("Cet emprunt n'est plus actif");
        }

        borrow.setStatus(BorrowStatus.RETURNED);
        borrow.setReturnDate(LocalDateTime.now());
        BookItem item = borrow.getBookItem();
        item.setStatus(BookItemStatus.AVAILABLE);
        bookItemRepository.save(item);

        return toResponse(borrowRepository.save(borrow));
    }

    public BorrowResponse renewBorrow(Long borrowId) {
        Borrow borrow = borrowRepository.findById(borrowId)
            .orElseThrow(() -> new ResourceNotFoundException("Emprunt non trouvé avec l'id : " + borrowId));

        if (borrow.getStatus() != BorrowStatus.ACTIVE) {
            throw new BusinessException("Seuls les emprunts actifs peuvent être renouvelés");
        }

        if (borrow.getRenewalCount() >= 3) {
            throw new BusinessException("Limite de renouvellement atteinte (max 3 fois)");
        }

        borrow.setRenewalCount(borrow.getRenewalCount() + 1);
        return toResponse(borrowRepository.save(borrow));
    }

    public Page<BorrowResponse> findAll(Pageable pageable) {
        return borrowRepository.findAll(pageable).map(this::toResponse);
    }

    public BorrowResponse findById(Long id) {
        return borrowRepository.findById(id)
            .map(this::toResponse)
            .orElseThrow(() -> new ResourceNotFoundException("Emprunt non trouvé avec l'id : " + id));
    }

    private BorrowResponse toResponse(Borrow borrow) {
        BorrowResponse r = new BorrowResponse();
        r.setId(borrow.getId());
        r.setRenewalCount(borrow.getRenewalCount());
        r.setBorrowDate(borrow.getBorrowDate());
        r.setDueDate(borrow.getDueDate());
        r.setReturnDate(borrow.getReturnDate());
        r.setStatus(borrow.getStatus());
        if (borrow.getMember() != null) {
            r.setMemberId(borrow.getMember().getId());
            r.setMemberEmail(borrow.getMember().getEmail());
        }
        if (borrow.getBookItem() != null) {
            r.setBookItemId(borrow.getBookItem().getId());
            r.setBookItemBarcode(borrow.getBookItem().getBarcode());
        }
        return r;
    }
}
