package supnum.projet.Library.data.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import supnum.projet.Library.data.entities.Reservation;
import supnum.projet.Library.data.enums.ReservationStatus;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    
    @Query("SELECT r FROM Reservation r WHERE r.deleted = false AND r.member.id = :memberId ORDER BY r.createdAt DESC")
    Page<Reservation> findByMemberIdActive(Long memberId, Pageable pageable);
    
    @Query("SELECT r FROM Reservation r WHERE r.deleted = false AND r.book.id = :bookId ORDER BY r.queuePosition ASC")
    Page<Reservation> findByBookIdActive(Long bookId, Pageable pageable);
    
    @Query("SELECT r FROM Reservation r WHERE r.deleted = false AND r.member.id = :memberId AND r.book.id = :bookId AND r.status = 'PENDING'")
    Optional<Reservation> findPendingReservation(Long memberId, Long bookId);
    
    @Query("SELECT r FROM Reservation r WHERE r.deleted = false AND r.book.id = :bookId AND r.status = 'PENDING' ORDER BY r.queuePosition ASC LIMIT 1")
    Optional<Reservation> findFirstPendingReservation(Long bookId);
    
    @Query("SELECT r FROM Reservation r WHERE r.deleted = false AND r.status = :status ORDER BY r.createdAt DESC")
    Page<Reservation> findByStatusActive(ReservationStatus status, Pageable pageable);
}
