package supnum.projet.Library.data.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import supnum.projet.Library.data.entities.Borrow;
import supnum.projet.Library.data.enums.BorrowStatus;
import java.util.Optional;

@Repository
public interface BorrowRepository extends JpaRepository<Borrow, Long> {
    
    @Query("SELECT b FROM Borrow b WHERE b.deleted = false AND b.member.id = :memberId ORDER BY b.borrowedAt DESC")
    Page<Borrow> findByMemberIdActive(Long memberId, Pageable pageable);
    
    @Query("SELECT b FROM Borrow b WHERE b.deleted = false AND b.bookItem.id = :bookItemId")
    Optional<Borrow> findByBookItemIdActive(Long bookItemId);
    
    @Query("SELECT b FROM Borrow b WHERE b.deleted = false AND b.status = :status ORDER BY b.borrowedAt DESC")
    Page<Borrow> findByStatusActive(BorrowStatus status, Pageable pageable);
    
    @Query("SELECT COUNT(b) FROM Borrow b WHERE b.deleted = false AND b.member.id = :memberId AND b.status = 'ACTIVE'")
    long countActiveBorrowsByMemberId(Long memberId);
}
