package supnum.projet.Library.dao;

import supnum.projet.Library.data.entities.Book;
import supnum.projet.Library.data.repositories.BookRepository;
import supnum.projet.Library.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class BookDao {

    private final BookRepository repository;

    public BookDao(BookRepository repository) {
        this.repository = repository;
    }

    public List<Book> findAll() {
        return repository.findAll();
    }

    public Book findById(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Livre non trouvé avec l'id : " + id));
    }

    public Optional<Book> findByIsbn(String isbn) {
        return repository.findByIsbn(isbn);
    }

    public Book save(Book book) {
        return repository.save(book);
    }

    public void softDelete(Long id) {
        Book book = findById(id);
        book.setDeleted(true);
        repository.save(book);
    }
}
