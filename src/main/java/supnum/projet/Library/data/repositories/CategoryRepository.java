package supnum.projet.Library.data.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import supnum.projet.Library.data.entities.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    
    @Query("SELECT c FROM Category c WHERE c.deleted = false ORDER BY c.name")
    Page<Category> findAllActive(Pageable pageable);
    
    @Query("SELECT c FROM Category c WHERE c.deleted = false AND c.name = :name")
    Category findByNameActive(String name);
}
