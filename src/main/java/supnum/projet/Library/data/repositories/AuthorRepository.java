package supnum.projet.Library.data.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import supnum.projet.Library.data.entities.Author;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
    
    @Query("SELECT a FROM Author a WHERE a.deleted = false ORDER BY a.name")
    Page<Author> findAllActive(Pageable pageable);
    
    @Query("SELECT a FROM Author a WHERE a.deleted = false AND a.name LIKE %:name%")
    Page<Author> findByNameContainingActive(String name, Pageable pageable);
}
