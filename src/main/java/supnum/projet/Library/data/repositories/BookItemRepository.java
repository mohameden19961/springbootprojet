package supnum.projet.Library.data.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import supnum.projet.Library.data.entities.BookItem;
import supnum.projet.Library.data.enums.BookItemStatus;

@Repository
public interface BookItemRepository extends JpaRepository<BookItem, Long> {
    
    @Query("SELECT bi FROM BookItem bi WHERE bi.deleted = false AND bi.barcode = :barcode")
    BookItem findByBarcodeActive(String barcode);
    
    @Query("SELECT bi FROM BookItem bi WHERE bi.deleted = false AND bi.status = :status")
    Page<BookItem> findByStatusActive(BookItemStatus status, Pageable pageable);
    
    @Query("SELECT bi FROM BookItem bi WHERE bi.deleted = false AND bi.book.id = :bookId")
    Page<BookItem> findByBookIdActive(Long bookId, Pageable pageable);
    
    @Query("SELECT COUNT(bi) FROM BookItem bi WHERE bi.deleted = false AND bi.book.id = :bookId AND bi.status = 'AVAILABLE'")
    long countAvailableByBookId(Long bookId);
}
