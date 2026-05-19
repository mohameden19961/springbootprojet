package supnum.projet.Library.data.repositories;

import supnum.projet.Library.data.entities.Borrow;
import supnum.projet.Library.data.entities.Member;
import supnum.projet.Library.data.entities.enums.BorrowStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BorrowRepository extends JpaRepository<Borrow, Long> {
    long countByMemberAndStatus(Member member, BorrowStatus status);
}
