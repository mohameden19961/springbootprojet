package supnum.projet.Library.services;

import supnum.projet.Library.data.entities.Book;
import supnum.projet.Library.data.entities.BookItem;
import supnum.projet.Library.data.entities.enums.BookItemStatus;
import supnum.projet.Library.dto.BookItemDTO;
import supnum.projet.Library.data.repositories.BookItemRepository;
import supnum.projet.Library.data.repositories.BookRepository;
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

    public List<BookItem> findAllByBook(Long bookId) {
        Book book = bookRepository.findById(bookId)
            .orElseThrow(() -> new ResourceNotFoundException("Livre non trouvé"));
        return book.getBookItems();
    }

    public BookItem findById(Long id) {
        return bookItemRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Exemplaire non trouvé avec l'id : " + id));
    }

    public BookItem create(Long bookId, BookItemDTO dto) {
        Book book = bookRepository.findById(bookId)
            .orElseThrow(() -> new ResourceNotFoundException("Livre non trouvé"));

        if (bookItemRepository.findByBarcode(dto.getBarcode()).isPresent()) {
            throw new RuntimeException("Un exemplaire avec ce code-barres existe déjà");
        }

        BookItem item = new BookItem();
        item.setBarcode(dto.getBarcode());
        item.setStatus(dto.getStatus() != null ? dto.getStatus() : BookItemStatus.AVAILABLE);
        item.setBook(book);

        return bookItemRepository.save(item);
    }

    public BookItem update(Long id, BookItemDTO dto) {
        BookItem item = findById(id);

        if (!item.getBarcode().equals(dto.getBarcode()) && bookItemRepository.findByBarcode(dto.getBarcode()).isPresent()) {
            throw new RuntimeException("Un exemplaire avec ce code-barres existe déjà");
        }

        item.setBarcode(dto.getBarcode());
        if (dto.getStatus() != null) {
            item.setStatus(dto.getStatus());
        }

        return bookItemRepository.save(item);
    }

    public void delete(Long id) {
        BookItem item = findById(id);
        item.setDeleted(true);
        bookItemRepository.save(item);
    }
}
