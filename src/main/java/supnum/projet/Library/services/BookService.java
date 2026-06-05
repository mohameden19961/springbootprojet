package supnum.projet.Library.services;

import supnum.projet.Library.data.entities.*;
import supnum.projet.Library.dto.BookDTO;
import supnum.projet.Library.dto.response.BookResponse;
import supnum.projet.Library.data.repositories.*;
import supnum.projet.Library.exceptions.DuplicateResourceException;
import supnum.projet.Library.exceptions.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BookService {

    private final BookRepository bookRepository;
    private final LanguageRepository languageRepository;
    private final CategoryRepository categoryRepository;
    private final PublisherRepository publisherRepository;

    public BookService(BookRepository bookRepo, LanguageRepository langRepo, CategoryRepository catRepo, PublisherRepository pubRepo) {
        this.bookRepository = bookRepo;
        this.languageRepository = langRepo;
        this.categoryRepository = catRepo;
        this.publisherRepository = pubRepo;
    }

    public Page<BookResponse> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable).map(this::toResponse);
    }

    public BookResponse create(BookDTO dto) {
        if(bookRepository.findByIsbn(dto.getIsbn()).isPresent()) {
            throw new DuplicateResourceException("Un livre avec cet ISBN existe déjà");
        }
        Language lang = languageRepository.findById(dto.getLanguageCode())
            .orElseThrow(() -> new ResourceNotFoundException("Langue introuvable avec le code : " + dto.getLanguageCode()));
        Category cat = categoryRepository.findById(dto.getCategoryId())
            .orElseThrow(() -> new ResourceNotFoundException("Catégorie introuvable avec l'id : " + dto.getCategoryId()));
        Publisher pub = publisherRepository.findById(dto.getPublisherId())
            .orElseThrow(() -> new ResourceNotFoundException("Éditeur introuvable avec l'id : " + dto.getPublisherId()));

        Book book = Book.builder()
            .title(dto.getTitle())
            .isbn(dto.getIsbn())
            .language(lang)
            .category(cat)
            .publisher(pub)
            .build();

        return toResponse(bookRepository.save(book));
    }

    public BookResponse findById(Long id) {
        return bookRepository.findById(id)
            .map(this::toResponse)
            .orElseThrow(() -> new ResourceNotFoundException("Livre non trouvé avec l'id : " + id));
    }

    public BookResponse update(Long id, BookDTO dto) {
        Book book = bookRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Livre non trouvé avec l'id : " + id));
        if (!book.getIsbn().equals(dto.getIsbn()) && bookRepository.findByIsbn(dto.getIsbn()).isPresent()) {
            throw new DuplicateResourceException("Un livre avec cet ISBN existe déjà");
        }
        Language lang = languageRepository.findById(dto.getLanguageCode())
            .orElseThrow(() -> new ResourceNotFoundException("Langue introuvable avec le code : " + dto.getLanguageCode()));
        Category cat = categoryRepository.findById(dto.getCategoryId())
            .orElseThrow(() -> new ResourceNotFoundException("Catégorie introuvable avec l'id : " + dto.getCategoryId()));
        Publisher pub = publisherRepository.findById(dto.getPublisherId())
            .orElseThrow(() -> new ResourceNotFoundException("Éditeur introuvable avec l'id : " + dto.getPublisherId()));

        book.setTitle(dto.getTitle());
        book.setIsbn(dto.getIsbn());
        book.setLanguage(lang);
        book.setCategory(cat);
        book.setPublisher(pub);

        return toResponse(bookRepository.save(book));
    }

    public void delete(Long id) {
        Book book = bookRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Livre non trouvé avec l'id : " + id));
        book.setDeleted(true);
        bookRepository.save(book);
    }

    private BookResponse toResponse(Book book) {
        BookResponse r = new BookResponse();
        r.setId(book.getId());
        r.setTitle(book.getTitle());
        r.setIsbn(book.getIsbn());
        if (book.getLanguage() != null) {
            r.setLanguageCode(book.getLanguage().getCode());
            r.setLanguageName(book.getLanguage().getName());
        }
        if (book.getCategory() != null) {
            r.setCategoryId(book.getCategory().getId());
            r.setCategoryName(book.getCategory().getName());
        }
        if (book.getPublisher() != null) {
            r.setPublisherId(book.getPublisher().getId());
            r.setPublisherName(book.getPublisher().getName());
        }
        return r;
    }
}
