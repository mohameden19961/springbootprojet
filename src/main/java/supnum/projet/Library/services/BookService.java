package supnum.projet.Library.services;

import supnum.projet.Library.data.entities.*;
import supnum.projet.Library.dto.BookDTO;
import supnum.projet.Library.data.repositories.*;
import supnum.projet.Library.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public Book create(BookDTO dto) {
        if(bookRepository.findByIsbn(dto.getIsbn()).isPresent()) {
            throw new RuntimeException("Un livre avec cet ISBN existe déjà");
        }
        Language lang = languageRepository.findById(dto.getLanguageCode())
            .orElseThrow(() -> new ResourceNotFoundException("Langue introuvable"));
        Category cat = categoryRepository.findById(dto.getCategoryId())
            .orElseThrow(() -> new ResourceNotFoundException("Catégorie introuvable"));
        Publisher pub = publisherRepository.findById(dto.getPublisherId())
            .orElseThrow(() -> new ResourceNotFoundException("Éditeur introuvable"));

        Book book = new Book();
        book.setTitle(dto.getTitle());
        book.setIsbn(dto.getIsbn());
        book.setLanguage(lang);
        book.setCategory(cat);
        book.setPublisher(pub);

        return bookRepository.save(book);
    }

    public Book findById(Long id) {
        return bookRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Livre non trouvé avec l'id : " + id));
    }

    public Book update(Long id, BookDTO dto) {
        Book book = findById(id);
        if (!book.getIsbn().equals(dto.getIsbn()) && bookRepository.findByIsbn(dto.getIsbn()).isPresent()) {
            throw new RuntimeException("Un livre avec cet ISBN existe déjà");
        }
        Language lang = languageRepository.findById(dto.getLanguageCode())
            .orElseThrow(() -> new ResourceNotFoundException("Langue introuvable"));
        Category cat = categoryRepository.findById(dto.getCategoryId())
            .orElseThrow(() -> new ResourceNotFoundException("Catégorie introuvable"));
        Publisher pub = publisherRepository.findById(dto.getPublisherId())
            .orElseThrow(() -> new ResourceNotFoundException("Éditeur introuvable"));

        book.setTitle(dto.getTitle());
        book.setIsbn(dto.getIsbn());
        book.setLanguage(lang);
        book.setCategory(cat);
        book.setPublisher(pub);

        return bookRepository.save(book);
    }

    public void delete(Long id) {
        Book book = findById(id);
        book.setDeleted(true);
        bookRepository.save(book);
    }
}
