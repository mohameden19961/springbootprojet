package supnum.projet.Library.data.repositories;

import supnum.projet.Library.data.entities.BookItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookItemRepository extends JpaRepository<BookItem, Long> {
    Optional<BookItem> findByBarcode(String barcode);
}
