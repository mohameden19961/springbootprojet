package supnum.projet.Library.dao;

import supnum.projet.Library.data.entities.Book;
import supnum.projet.Library.data.entities.Author;
import supnum.projet.Library.data.entities.BookAuthor;
import supnum.projet.Library.data.entities.BookAuthor.BookAuthorId;
import supnum.projet.Library.data.entities.enums.AuthorRole;
import supnum.projet.Library.data.repositories.BookAuthorRepository;
import supnum.projet.Library.data.repositories.BookRepository;
import supnum.projet.Library.data.repositories.AuthorRepository;
import supnum.projet.Library.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BookAuthorDao {

    private final BookAuthorRepository bookAuthorRepository;
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public BookAuthorDao(BookAuthorRepository bookAuthorRepository,
                         BookRepository bookRepository,
                         AuthorRepository authorRepository) {
        this.bookAuthorRepository = bookAuthorRepository;
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    public List<BookAuthor> findAllByBook(Long bookId) {
        return bookAuthorRepository.findAll().stream()
            .filter(ba -> ba.getBook() != null && ba.getBook().getId().equals(bookId))
            .toList();
    }

    public BookAuthor addAuthorToBook(Long bookId, Long authorId, AuthorRole role) {
        Book book = bookRepository.findById(bookId)
            .orElseThrow(() -> new ResourceNotFoundException("Livre non trouvé"));
        Author author = authorRepository.findById(authorId)
            .orElseThrow(() -> new ResourceNotFoundException("Auteur non trouvé"));

        BookAuthorId id = new BookAuthorId();
        id.setBookId(bookId);
        id.setAuthorId(authorId);

        BookAuthor bookAuthor = new BookAuthor();
        bookAuthor.setId(id);
        bookAuthor.setBook(book);
        bookAuthor.setAuthor(author);
        bookAuthor.setRole(role);

        return bookAuthorRepository.save(bookAuthor);
    }

    public boolean existsById(BookAuthorId id) {
        return bookAuthorRepository.existsById(id);
    }

    public void deleteById(BookAuthorId id) {
        bookAuthorRepository.deleteById(id);
    }
}
