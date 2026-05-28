package supnum.projet.Library.services;

import supnum.projet.Library.data.entities.*;
import supnum.projet.Library.data.entities.BookAuthor.BookAuthorId;
import supnum.projet.Library.data.entities.enums.AuthorRole;
import supnum.projet.Library.data.repositories.*;
import supnum.projet.Library.dto.response.BookAuthorResponse;
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

    

    public List<BookAuthorResponse> findAllByBook(Long bookId) {
        return bookAuthorRepository.findByBookId(bookId).stream()
            .map(this::toResponse)
            .toList();
    }

    public BookAuthorResponse addAuthorToBook(Long bookId, Long authorId, AuthorRole role) {
        Book book = bookRepository.findById(bookId)
            .orElseThrow(() -> new ResourceNotFoundException("Livre non trouvé"));
        Author author = authorRepository.findById(authorId)
            .orElseThrow(() -> new ResourceNotFoundException("Auteur non trouvé"));

        BookAuthorId id = new BookAuthorId(bookId, authorId);

        BookAuthor bookAuthor = BookAuthor.builder()
            .id(id)
            .book(book)
            .author(author)
            .role(role)
            .build();

        return toResponse(bookAuthorRepository.save(bookAuthor));
    }

    private BookAuthorResponse toResponse(BookAuthor ba) {
        BookAuthorResponse r = new BookAuthorResponse();
        r.setRole(ba.getRole());
        if (ba.getBook() != null) {
            r.setBookId(ba.getBook().getId());
            r.setBookTitle(ba.getBook().getTitle());
        }
        if (ba.getAuthor() != null) {
            r.setAuthorId(ba.getAuthor().getId());
            r.setAuthorName(ba.getAuthor().getName());
        }
        return r;
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
