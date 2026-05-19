package supnum.projet.Library.controllers;

import supnum.projet.Library.dto.BookDTO;
import supnum.projet.Library.dto.BookResponseDTO;
import supnum.projet.Library.services.BookService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public List<BookResponseDTO> getAll() {
        return service.findAll().stream().map(BookResponseDTO::from).toList();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BookResponseDTO> create(@Valid @RequestBody BookDTO dto) {
        return ResponseEntity.ok(BookResponseDTO.from(service.create(dto)));
    }
}
