package supnum.projet.Library.data.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import supnum.projet.Library.data.entities.Publisher;

@Repository
public interface PublisherRepository extends JpaRepository<Publisher, Long> {
    
    @Query("SELECT p FROM Publisher p WHERE p.deleted = false ORDER BY p.name")
    Page<Publisher> findAllActive(Pageable pageable);
    
    @Query("SELECT p FROM Publisher p WHERE p.deleted = false AND p.email = :email")
    Publisher findByEmailActive(String email);
    
    @Query("SELECT p FROM Publisher p WHERE p.deleted = false AND p.name = :name")
    Publisher findByNameActive(String name);
}
