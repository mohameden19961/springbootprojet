package supnum.projet.Library.data.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import supnum.projet.Library.data.entities.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    
    @Query("SELECT b FROM Book b WHERE b.deleted = false ORDER BY b.title")
    Page<Book> findAllActive(Pageable pageable);
    
    @Query("SELECT b FROM Book b WHERE b.deleted = false AND b.isbn = :isbn")
    Book findByIsbnActive(String isbn);
    
    @Query("SELECT b FROM Book b WHERE b.deleted = false AND b.title LIKE %:title%")
    Page<Book> findByTitleContainingActive(String title, Pageable pageable);
    
    @Query("SELECT b FROM Book b JOIN b.authors a WHERE b.deleted = false AND a.id = :authorId")
    Page<Book> findByAuthorIdActive(Long authorId, Pageable pageable);
    
    @Query("SELECT b FROM Book b WHERE b.deleted = false AND b.category.id = :categoryId")
    Page<Book> findByCategoryIdActive(Long categoryId, Pageable pageable);
    
    @Query("SELECT b FROM Book b WHERE b.deleted = false AND b.language.code = :languageCode")
    Page<Book> findByLanguageCodeActive(String languageCode, Pageable pageable);
}
