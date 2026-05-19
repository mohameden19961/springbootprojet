package supnum.projet.Library.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import supnum.projet.Library.dto.BookDTO;
import supnum.projet.Library.dto.BookItemDTO;
import supnum.projet.Library.services.BookService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
@Validated
public class BookController {
    
    private final BookService bookService;
    
    @PostMapping
    public ResponseEntity<BookDTO> createBook(@Valid @RequestBody BookDTO bookDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookService.createBook(bookDTO));
    }
    
    @GetMapping
    public ResponseEntity<Page<BookDTO>> getAllBooks(Pageable pageable) {
        return ResponseEntity.ok(bookService.getAllBooks(pageable));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> getBookById(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.getBookById(id));
    }
    
    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<BookDTO> getBookByIsbn(@PathVariable String isbn) {
        return ResponseEntity.ok(bookService.getBookByIsbn(isbn));
    }
    
    @GetMapping("/search/title")
    public ResponseEntity<Page<BookDTO>> searchByTitle(
            @RequestParam String title,
            Pageable pageable) {
        return ResponseEntity.ok(bookService.searchBooksByTitle(title, pageable));
    }
    
    @GetMapping("/filter/category/{categoryId}")
    public ResponseEntity<Page<BookDTO>> getByCategory(
            @PathVariable Long categoryId,
            Pageable pageable) {
        return ResponseEntity.ok(bookService.findBooksByCategory(categoryId, pageable));
    }
    
    @GetMapping("/filter/author/{authorId}")
    public ResponseEntity<Page<BookDTO>> getByAuthor(
            @PathVariable Long authorId,
            Pageable pageable) {
        return ResponseEntity.ok(bookService.findBooksByAuthor(authorId, pageable));
    }
    
    @GetMapping("/filter/language/{languageCode}")
    public ResponseEntity<Page<BookDTO>> getByLanguage(
            @PathVariable String languageCode,
            Pageable pageable) {
        return ResponseEntity.ok(bookService.findBooksByLanguage(languageCode, pageable));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<BookDTO> updateBook(
            @PathVariable Long id,
            @Valid @RequestBody BookDTO bookDTO) {
        return ResponseEntity.ok(bookService.updateBook(id, bookDTO));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
    
    // Book Items endpoints
    
    @PostMapping("/{bookId}/items")
    public ResponseEntity<BookItemDTO> addBookItem(
            @PathVariable Long bookId,
            @Valid @RequestBody BookItemDTO bookItemDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(bookService.createBookItem(bookId, bookItemDTO));
    }
    
    @GetMapping("/{bookId}/items")
    public ResponseEntity<Page<BookItemDTO>> getBookItems(
            @PathVariable Long bookId,
            Pageable pageable) {
        return ResponseEntity.ok(bookService.getBookItemsByBook(bookId, pageable));
    }
    
    @GetMapping("/items/{itemId}")
    public ResponseEntity<BookItemDTO> getBookItem(@PathVariable Long itemId) {
        return ResponseEntity.ok(bookService.getBookItemById(itemId));
    }
    
    @GetMapping("/{bookId}/available-count")
    public ResponseEntity<Long> getAvailableCount(@PathVariable Long bookId) {
        return ResponseEntity.ok(bookService.countAvailableItemsByBook(bookId));
    }
}
