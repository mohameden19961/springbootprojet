package supnum.projet.Library.dao;

import supnum.projet.Library.data.entities.BookItem;
import supnum.projet.Library.data.repositories.BookItemRepository;
import supnum.projet.Library.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BookItemDao {

    private final BookItemRepository repository;

    public BookItemDao(BookItemRepository repository) {
        this.repository = repository;
    }

    public List<BookItem> findAll() {
        return repository.findAll();
    }

    public BookItem findById(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Exemplaire non trouvé avec l'id : " + id));
    }

    public BookItem findByBarcode(String barcode) {
        return repository.findByBarcode(barcode)
            .orElseThrow(() -> new ResourceNotFoundException("Exemplaire physique introuvable"));
    }

    public BookItem save(BookItem bookItem) {
        return repository.save(bookItem);
    }
}
