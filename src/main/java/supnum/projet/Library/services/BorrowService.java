package supnum.projet.Library.services;

import supnum.projet.Library.data.entities.*;
import supnum.projet.Library.data.entities.enums.*;
import supnum.projet.Library.data.repositories.*;
import supnum.projet.Library.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    public Borrow borrowBook(Long memberId, String barcode) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new ResourceNotFoundException("Membre introuvable"));

        BookItem item = bookItemRepository.findByBarcode(barcode)
            .orElseThrow(() -> new ResourceNotFoundException("Exemplaire physique introuvable"));

        if (item.getStatus() != BookItemStatus.AVAILABLE) {
            throw new RuntimeException("L'exemplaire n'est pas disponible");
        }

        long activeBorrows = borrowRepository.countByMemberAndStatus(member, BorrowStatus.ACTIVE);
        if (activeBorrows >= member.getMaxBorrows()) {
            throw new RuntimeException("Le membre a atteint sa limite maximale d'emprunts");
        }

        item.setStatus(BookItemStatus.BORROWED);
        bookItemRepository.save(item);

        Borrow borrow = new Borrow();
        borrow.setMember(member);
        borrow.setBookItem(item);
        borrow.setStatus(BorrowStatus.ACTIVE);
        borrow.setRenewalCount(0);

        // Annuler automatiquement la réservation PENDING du membre pour ce livre
        Book book = item.getBook();
        if (book != null) {
            List<Reservation> pending = reservationRepository
                .findByMemberAndBookAndStatus(member, book, ReservationStatus.PENDING);
            pending.forEach(r -> r.setStatus(ReservationStatus.CANCELLED));
            reservationRepository.saveAll(pending);
        }

        return borrowRepository.save(borrow);
    }

    public Borrow returnBook(Long borrowId) {
        Borrow borrow = borrowRepository.findById(borrowId)
            .orElseThrow(() -> new ResourceNotFoundException("Emprunt introuvable"));

        if (borrow.getStatus() != BorrowStatus.ACTIVE) {
            throw new RuntimeException("Cet emprunt n'est plus actif");
        }

        borrow.setStatus(BorrowStatus.RETURNED);
        BookItem item = borrow.getBookItem();
        item.setStatus(BookItemStatus.AVAILABLE);
        bookItemRepository.save(item);

        return borrowRepository.save(borrow);
    }

    public Borrow renewBorrow(Long borrowId) {
        Borrow borrow = borrowRepository.findById(borrowId)
            .orElseThrow(() -> new ResourceNotFoundException("Emprunt introuvable"));

        if (borrow.getStatus() != BorrowStatus.ACTIVE) {
            throw new RuntimeException("Seuls les emprunts actifs peuvent être renouvelés");
        }

        if (borrow.getRenewalCount() >= 3) {
            throw new RuntimeException("Limite de renouvellement atteinte (max 3 fois)");
        }

        borrow.setRenewalCount(borrow.getRenewalCount() + 1);
        return borrowRepository.save(borrow);
    }

    public List<Borrow> findAll() {
        return borrowRepository.findAll();
    }

    public Borrow findById(Long id) {
        return borrowRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Emprunt non trouvé avec l'id : " + id));
    }
}
