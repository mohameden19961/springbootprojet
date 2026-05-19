package supnum.projet.Library.data.repositories;

import supnum.projet.Library.data.entities.Reservation;
import supnum.projet.Library.data.entities.Book;
import supnum.projet.Library.data.entities.enums.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByBookAndStatusOrderByQueuePositionAsc(Book book, ReservationStatus status);

    @Query("SELECT COALESCE(MAX(r.queuePosition), 0) FROM Reservation r WHERE r.book = :book AND r.status = 'PENDING'")
    Integer findMaxQueuePositionForBook(@Param("book") Book book);
}
