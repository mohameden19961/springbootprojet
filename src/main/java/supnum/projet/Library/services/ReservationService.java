package supnum.projet.Library.services;

import supnum.projet.Library.data.entities.*;
import supnum.projet.Library.data.entities.enums.ReservationStatus;
import supnum.projet.Library.dto.ReservationDTO;
import supnum.projet.Library.data.repositories.*;
import supnum.projet.Library.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;

    public ReservationService(ReservationRepository resRepo, BookRepository bookRepo, MemberRepository memberRepo) {
        this.reservationRepository = resRepo;
        this.bookRepository = bookRepo;
        this.memberRepository = memberRepo;
    }

    public Reservation reserve(ReservationDTO dto) {
        Member member = memberRepository.findById(dto.getMemberId())
            .orElseThrow(() -> new ResourceNotFoundException("Membre non trouvé"));
        Book book = bookRepository.findById(dto.getBookId())
            .orElseThrow(() -> new ResourceNotFoundException("Livre non trouvé"));

        int nextPosition = reservationRepository.findMaxQueuePositionForBook(book) + 1;

        Reservation reservation = new Reservation();
        reservation.setMember(member);
        reservation.setBook(book);
        reservation.setStatus(ReservationStatus.PENDING);
        reservation.setQueuePosition(nextPosition);

        return reservationRepository.save(reservation);
    }

    public Reservation cancel(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
            .orElseThrow(() -> new ResourceNotFoundException("Réservation non trouvée"));

        if (reservation.getStatus() != ReservationStatus.PENDING) {
            throw new RuntimeException("Seules les réservations en attente (PENDING) peuvent être annulées");
        }

        reservation.setStatus(ReservationStatus.CANCELLED);
        return reservationRepository.save(reservation);
    }

    public List<Reservation> getQueueForBook(Long bookId) {
        Book book = bookRepository.findById(bookId)
            .orElseThrow(() -> new ResourceNotFoundException("Livre non trouvé"));
        return reservationRepository.findByBookAndStatusOrderByQueuePositionAsc(book, ReservationStatus.PENDING);
    }

    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    public Reservation findById(Long id) {
        return reservationRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Réservation non trouvée avec l'id : " + id));
    }
}
