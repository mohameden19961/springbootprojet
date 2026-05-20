package supnum.projet.Library.controllers;

import supnum.projet.Library.data.entities.BookItem;
import supnum.projet.Library.dto.BookItemDTO;
import supnum.projet.Library.services.BookItemService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books/{bookId}/items")
public class BookItemController {

    private final BookItemService service;

    public BookItemController(BookItemService service) {
        this.service = service;
    }

    @GetMapping
    public List<BookItem> getAll(@PathVariable Long bookId) {
        return service.findAllByBook(bookId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookItem> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<BookItem> create(@PathVariable Long bookId, @Valid @RequestBody BookItemDTO dto) {
        return ResponseEntity.ok(service.create(bookId, dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookItem> update(@PathVariable Long id, @Valid @RequestBody BookItemDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
