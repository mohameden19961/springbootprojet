package supnum.projet.Library.controllers;

import supnum.projet.Library.dto.BookItemDTO;
import supnum.projet.Library.dto.response.BookItemResponse;
import supnum.projet.Library.services.BookItemService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
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
    public List<BookItemResponse> getAll(@PathVariable Long bookId) {
        return service.findAllByBook(bookId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookItemResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<BookItemResponse> create(@PathVariable Long bookId, @Valid @RequestBody BookItemDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(bookId, dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookItemResponse> update(@PathVariable Long id, @Valid @RequestBody BookItemDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
