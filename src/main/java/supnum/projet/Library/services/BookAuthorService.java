package supnum.projet.Library.services;

import supnum.projet.Library.data.entities.*;
import supnum.projet.Library.data.entities.BookAuthor.BookAuthorId;
import supnum.projet.Library.data.entities.enums.AuthorRole;
import supnum.projet.Library.data.repositories.*;
import supnum.projet.Library.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class BookAuthorService {

    private final BookAuthorRepository bookAuthorRepository;
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public BookAuthorService(BookAuthorRepository bookAuthorRepo, BookRepository bookRepo, AuthorRepository authorRepo) {
        this.bookAuthorRepository = bookAuthorRepo;
        this.bookRepository = bookRepo;
        this.authorRepository = authorRepo;
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

    public void removeAuthorFromBook(Long bookId, Long authorId) {
        BookAuthorId id = new BookAuthorId();
        id.setBookId(bookId);
        id.setAuthorId(authorId);
        if (!bookAuthorRepository.existsById(id)) {
            throw new ResourceNotFoundException("Association livre-auteur non trouvée");
        }
        bookAuthorRepository.deleteById(id);
    }
}
