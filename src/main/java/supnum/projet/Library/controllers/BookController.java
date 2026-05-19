package supnum.projet.Library.controllers;

import supnum.projet.Library.data.entities.Book;
import supnum.projet.Library.dto.BookDTO;
import supnum.projet.Library.services.BookService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService service;

    public BookController(BookService service) {
        this.service = service;
    }

    @GetMapping
    public List<Book> getAll() {
        return service.findAll();
    }

    @PostMapping
    public ResponseEntity<Book> create(@Valid @RequestBody BookDTO dto) {
        return ResponseEntity.ok(service.create(dto));
    }
}
