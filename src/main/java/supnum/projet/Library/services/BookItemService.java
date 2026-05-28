package supnum.projet.Library.services;

import supnum.projet.Library.data.entities.Book;
import supnum.projet.Library.data.entities.BookItem;
import supnum.projet.Library.data.entities.enums.BookItemStatus;
import supnum.projet.Library.dto.BookItemDTO;
import supnum.projet.Library.dto.response.BookItemResponse;
import supnum.projet.Library.data.repositories.BookItemRepository;
import supnum.projet.Library.data.repositories.BookRepository;
import supnum.projet.Library.exceptions.DuplicateResourceException;
import supnum.projet.Library.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class BookItemService {

    private final BookItemRepository bookItemRepository;
    private final BookRepository bookRepository;

    public BookItemService(BookItemRepository bookItemRepo, BookRepository bookRepo) {
        this.bookItemRepository = bookItemRepo;
        this.bookRepository = bookRepo;
    }

    public List<BookItemResponse> findAllByBook(Long bookId) {
        Book book = bookRepository.findById(bookId)
            .orElseThrow(() -> new ResourceNotFoundException("Livre non trouvé avec l'id : " + bookId));
        return book.getBookItems().stream().map(this::toResponse).toList();
    }

    public BookItemResponse findById(Long id) {
        return bookItemRepository.findById(id)
            .map(this::toResponse)
            .orElseThrow(() -> new ResourceNotFoundException("Exemplaire non trouvé avec l'id : " + id));
    }

    public BookItemResponse create(Long bookId, BookItemDTO dto) {
        Book book = bookRepository.findById(bookId)
            .orElseThrow(() -> new ResourceNotFoundException("Livre non trouvé avec l'id : " + bookId));

        if (bookItemRepository.findByBarcode(dto.getBarcode()).isPresent()) {
            throw new DuplicateResourceException("Un exemplaire avec ce code-barres existe déjà");
        }

        BookItem item = BookItem.builder()
            .barcode(dto.getBarcode())
            .status(dto.getStatus() != null ? dto.getStatus() : BookItemStatus.AVAILABLE)
            .book(book)
            .build();

        return toResponse(bookItemRepository.save(item));
    }

    public BookItemResponse update(Long id, BookItemDTO dto) {
        BookItem item = bookItemRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Exemplaire non trouvé avec l'id : " + id));

        if (!item.getBarcode().equals(dto.getBarcode()) && bookItemRepository.findByBarcode(dto.getBarcode()).isPresent()) {
            throw new DuplicateResourceException("Un exemplaire avec ce code-barres existe déjà");
        }

        item.setBarcode(dto.getBarcode());
        if (dto.getStatus() != null) {
            item.setStatus(dto.getStatus());
        }

        return toResponse(bookItemRepository.save(item));
    }

    public void delete(Long id) {
        BookItem item = bookItemRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Exemplaire non trouvé avec l'id : " + id));
        item.setDeleted(true);
        bookItemRepository.save(item);
    }

    private BookItemResponse toResponse(BookItem item) {
        BookItemResponse r = new BookItemResponse();
        r.setId(item.getId());
        r.setBarcode(item.getBarcode());
        r.setStatus(item.getStatus());
        r.setVersion(item.getVersion());
        if (item.getBook() != null) {
            r.setBookId(item.getBook().getId());
        }
        return r;
    }
}
