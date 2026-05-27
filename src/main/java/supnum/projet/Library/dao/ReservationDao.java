package supnum.projet.Library.dao;

import supnum.projet.Library.data.entities.Book;
import supnum.projet.Library.data.entities.Member;
import supnum.projet.Library.data.entities.Reservation;
import supnum.projet.Library.data.entities.enums.ReservationStatus;
import supnum.projet.Library.data.repositories.ReservationRepository;
import supnum.projet.Library.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ReservationDao {

    private final ReservationRepository repository;

    public ReservationDao(ReservationRepository repository) {
        this.repository = repository;
    }

    public List<Reservation> findAll() {
        return repository.findAll();
    }

    public Reservation findById(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Réservation non trouvée avec l'id : " + id));
    }

    public Reservation save(Reservation reservation) {
        return repository.save(reservation);
    }

    public List<Reservation> findByBookAndStatusOrderByQueuePositionAsc(Book book, ReservationStatus status) {
        return repository.findByBookAndStatusOrderByQueuePositionAsc(book, status);
    }

    public int findMaxQueuePositionForBook(Book book) {
        Integer max = repository.findMaxQueuePositionForBook(book);
        return max != null ? max : 0;
    }

    public List<Reservation> findByMemberAndBookAndStatus(Member member, Book book, ReservationStatus status) {
        return repository.findByMemberAndBookAndStatus(member, book, status);
    }
}
