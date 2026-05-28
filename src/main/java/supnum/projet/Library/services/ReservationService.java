package supnum.projet.Library.services;

import supnum.projet.Library.data.entities.*;
import supnum.projet.Library.data.entities.enums.ReservationStatus;
import supnum.projet.Library.dto.ReservationDTO;
import supnum.projet.Library.dto.response.ReservationResponse;
import supnum.projet.Library.data.repositories.*;
import supnum.projet.Library.exceptions.BusinessException;
import supnum.projet.Library.exceptions.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public ReservationResponse reserve(ReservationDTO dto) {
        Member member = memberRepository.findById(dto.getMemberId())
            .orElseThrow(() -> new ResourceNotFoundException("Membre non trouvé avec l'id : " + dto.getMemberId()));
        Book book = bookRepository.findById(dto.getBookId())
            .orElseThrow(() -> new ResourceNotFoundException("Livre non trouvé avec l'id : " + dto.getBookId()));

        int nextPosition = reservationRepository.findMaxQueuePositionForBook(book) + 1;

        Reservation reservation = Reservation.builder()
            .member(member)
            .book(book)
            .status(ReservationStatus.PENDING)
            .queuePosition(nextPosition)
            .build();

        return toResponse(reservationRepository.save(reservation));
    }

    public ReservationResponse cancel(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
            .orElseThrow(() -> new ResourceNotFoundException("Réservation non trouvée avec l'id : " + reservationId));

        if (reservation.getStatus() != ReservationStatus.PENDING) {
            throw new BusinessException("Seules les réservations en attente (PENDING) peuvent être annulées");
        }

        reservation.setStatus(ReservationStatus.CANCELLED);
        return toResponse(reservationRepository.save(reservation));
    }

    public List<ReservationResponse> getQueueForBook(Long bookId) {
        Book book = bookRepository.findById(bookId)
            .orElseThrow(() -> new ResourceNotFoundException("Livre non trouvé avec l'id : " + bookId));
        return reservationRepository.findByBookAndStatusOrderByQueuePositionAsc(book, ReservationStatus.PENDING)
            .stream().map(this::toResponse).toList();
    }

    public Page<ReservationResponse> findAll(Pageable pageable) {
        return reservationRepository.findAll(pageable).map(this::toResponse);
    }

    public ReservationResponse findById(Long id) {
        return reservationRepository.findById(id)
            .map(this::toResponse)
            .orElseThrow(() -> new ResourceNotFoundException("Réservation non trouvée avec l'id : " + id));
    }

    private ReservationResponse toResponse(Reservation reservation) {
        ReservationResponse r = new ReservationResponse();
        r.setId(reservation.getId());
        r.setQueuePosition(reservation.getQueuePosition());
        r.setStatus(reservation.getStatus());
        if (reservation.getMember() != null) {
            r.setMemberId(reservation.getMember().getId());
            r.setMemberEmail(reservation.getMember().getEmail());
        }
        if (reservation.getBook() != null) {
            r.setBookId(reservation.getBook().getId());
            r.setBookTitle(reservation.getBook().getTitle());
        }
        return r;
    }
}
