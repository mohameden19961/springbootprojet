package supnum.projet.Library.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import supnum.projet.Library.data.entities.*;
import supnum.projet.Library.data.repositories.*;
import supnum.projet.Library.data.enums.BookItemStatus;
import supnum.projet.Library.dto.*;
import supnum.projet.Library.exceptions.ResourceNotFoundException;
import supnum.projet.Library.exceptions.DuplicateResourceException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BookService {
    
    private final BookRepository bookRepository;
    private final BookItemRepository bookItemRepository;
    private final LanguageRepository languageRepository;
    private final CategoryRepository categoryRepository;
    private final PublisherRepository publisherRepository;
    private final AuthorRepository authorRepository;
    
    // ============ BOOK OPERATIONS ============
    
    public BookDTO createBook(BookDTO bookDTO) {
        // Vérifier l'unicité de l'ISBN
        if (bookRepository.findByIsbnActive(bookDTO.getIsbn()) != null) {
            throw new DuplicateResourceException("Book with ISBN '" + bookDTO.getIsbn() + "' already exists");
        }
        
        // Récupérer les dépendances
        Language language = languageRepository.findById(bookDTO.getLanguageCode())
                .orElseThrow(() -> new ResourceNotFoundException("Language not found with code: " + bookDTO.getLanguageCode()));
        
        Category category = categoryRepository.findById(bookDTO.getCategoryId())
                .filter(c -> !c.getDeleted())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + bookDTO.getCategoryId()));
        
        Publisher publisher = publisherRepository.findById(bookDTO.getPublisherId())
                .filter(p -> !p.getDeleted())
                .orElseThrow(() -> new ResourceNotFoundException("Publisher not found with id: " + bookDTO.getPublisherId()));
        
        Book book = Book.builder()
                .title(bookDTO.getTitle())
                .isbn(bookDTO.getIsbn())
                .language(language)
                .category(category)
                .publisher(publisher)
                .deleted(false)
                .build();
        
        Book saved = bookRepository.save(book);
        return mapToDTO(saved);
    }
    
    @Transactional(readOnly = true)
    public Page<BookDTO> getAllBooks(Pageable pageable) {
        return bookRepository.findAllActive(pageable)
                .map(this::mapToDTO);
    }
    
    @Transactional(readOnly = true)
    public BookDTO getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .filter(b -> !b.getDeleted())
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
        return mapToDTO(book);
    }
    
    @Transactional(readOnly = true)
    public BookDTO getBookByIsbn(String isbn) {
        Book book = bookRepository.findByIsbnActive(isbn);
        if (book == null) {
            throw new ResourceNotFoundException("Book not found with ISBN: " + isbn);
        }
        return mapToDTO(book);
    }
    
    @Transactional(readOnly = true)
    public Page<BookDTO> searchBooksByTitle(String title, Pageable pageable) {
        return bookRepository.findByTitleContainingActive(title, pageable)
                .map(this::mapToDTO);
    }
    
    @Transactional(readOnly = true)
    public Page<BookDTO> findBooksByCategory(Long categoryId, Pageable pageable) {
        // Vérifier que la catégorie existe
        categoryRepository.findById(categoryId)
                .filter(c -> !c.getDeleted())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + categoryId));
        
        return bookRepository.findByCategoryIdActive(categoryId, pageable)
                .map(this::mapToDTO);
    }
    
    @Transactional(readOnly = true)
    public Page<BookDTO> findBooksByAuthor(Long authorId, Pageable pageable) {
        // Vérifier que l'auteur existe
        authorRepository.findById(authorId)
                .filter(a -> !a.getDeleted())
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with id: " + authorId));
        
        return bookRepository.findByAuthorIdActive(authorId, pageable)
                .map(this::mapToDTO);
    }
    
    @Transactional(readOnly = true)
    public Page<BookDTO> findBooksByLanguage(String languageCode, Pageable pageable) {
        // Vérifier que la langue existe
        languageRepository.findById(languageCode)
                .orElseThrow(() -> new ResourceNotFoundException("Language not found with code: " + languageCode));
        
        return bookRepository.findByLanguageCodeActive(languageCode, pageable)
                .map(this::mapToDTO);
    }
    
    public BookDTO updateBook(Long id, BookDTO bookDTO) {
        Book book = bookRepository.findById(id)
                .filter(b -> !b.getDeleted())
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
        
        // Vérifier ISBN si modifié
        if (!book.getIsbn().equals(bookDTO.getIsbn()) && 
            bookRepository.findByIsbnActive(bookDTO.getIsbn()) != null) {
            throw new DuplicateResourceException("Book with ISBN '" + bookDTO.getIsbn() + "' already exists");
        }
        
        Language language = languageRepository.findById(bookDTO.getLanguageCode())
                .orElseThrow(() -> new ResourceNotFoundException("Language not found with code: " + bookDTO.getLanguageCode()));
        
        Category category = categoryRepository.findById(bookDTO.getCategoryId())
                .filter(c -> !c.getDeleted())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + bookDTO.getCategoryId()));
        
        Publisher publisher = publisherRepository.findById(bookDTO.getPublisherId())
                .filter(p -> !p.getDeleted())
                .orElseThrow(() -> new ResourceNotFoundException("Publisher not found with id: " + bookDTO.getPublisherId()));
        
        book.setTitle(bookDTO.getTitle());
        book.setIsbn(bookDTO.getIsbn());
        book.setLanguage(language);
        book.setCategory(category);
        book.setPublisher(publisher);
        
        Book updated = bookRepository.save(book);
        return mapToDTO(updated);
    }
    
    public void deleteBook(Long id) {
        Book book = bookRepository.findById(id)
                .filter(b -> !b.getDeleted())
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
        
        book.setDeleted(true);
        bookRepository.save(book);
    }
    
    // ============ BOOK ITEM OPERATIONS ============
    
    public BookItemDTO createBookItem(Long bookId, BookItemDTO bookItemDTO) {
        Book book = bookRepository.findById(bookId)
                .filter(b -> !b.getDeleted())
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + bookId));
        
        // Vérifier l'unicité du barcode
        if (bookItemRepository.findByBarcodeActive(bookItemDTO.getBarcode()) != null) {
            throw new DuplicateResourceException("Book item with barcode '" + bookItemDTO.getBarcode() + "' already exists");
        }
        
        BookItem bookItem = BookItem.builder()
                .barcode(bookItemDTO.getBarcode())
                .status(BookItemStatus.AVAILABLE)
                .book(book)
                .deleted(false)
                .build();
        
        BookItem saved = bookItemRepository.save(bookItem);
        return mapBookItemToDTO(saved);
    }
    
    @Transactional(readOnly = true)
    public Page<BookItemDTO> getBookItemsByBook(Long bookId, Pageable pageable) {
        // Vérifier que le livre existe
        bookRepository.findById(bookId)
                .filter(b -> !b.getDeleted())
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + bookId));
        
        return bookItemRepository.findByBookIdActive(bookId, pageable)
                .map(this::mapBookItemToDTO);
    }
    
    @Transactional(readOnly = true)
    public BookItemDTO getBookItemById(Long id) {
        BookItem bookItem = bookItemRepository.findById(id)
                .filter(bi -> !bi.getDeleted())
                .orElseThrow(() -> new ResourceNotFoundException("Book item not found with id: " + id));
        return mapBookItemToDTO(bookItem);
    }
    
    @Transactional(readOnly = true)
    public long countAvailableItemsByBook(Long bookId) {
        return bookItemRepository.countAvailableByBookId(bookId);
    }
    
    // Mapper methods
    
    private BookDTO mapToDTO(Book book) {
        return BookDTO.builder()
                .id(book.getId())
                .title(book.getTitle())
                .isbn(book.getIsbn())
                .languageCode(book.getLanguage().getCode())
                .language(new LanguageDTO(book.getLanguage().getCode(), book.getLanguage().getName()))
                .categoryId(book.getCategory().getId())
                .category(CategoryDTO.builder().id(book.getCategory().getId()).name(book.getCategory().getName()).build())
                .publisherId(book.getPublisher().getId())
                .publisher(PublisherDTO.builder().id(book.getPublisher().getId()).name(book.getPublisher().getName()).email(book.getPublisher().getEmail()).build())
                .authors(book.getAuthors().stream()
                        .map(a -> AuthorDTO.builder().id(a.getId()).name(a.getName()).build())
                        .collect(Collectors.toSet()))
                .build();
    }
    
    private BookItemDTO mapBookItemToDTO(BookItem bookItem) {
        return BookItemDTO.builder()
                .id(bookItem.getId())
                .barcode(bookItem.getBarcode())
                .status(bookItem.getStatus())
                .bookId(bookItem.getBook().getId())
                .version(bookItem.getVersion())
                .build();
    }
}
