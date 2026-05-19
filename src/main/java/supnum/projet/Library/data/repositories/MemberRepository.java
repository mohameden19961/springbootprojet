package supnum.projet.Library.data.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import supnum.projet.Library.data.entities.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    
    @Query("SELECT m FROM Member m WHERE m.deleted = false ORDER BY m.email")
    Page<Member> findAllActive(Pageable pageable);
    
    @Query("SELECT m FROM Member m WHERE m.deleted = false AND m.email = :email")
    Member findByEmailActive(String email);
}
