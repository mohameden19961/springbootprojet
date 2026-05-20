package supnum.projet.Library.controllers;

import supnum.projet.Library.data.entities.Author;
import supnum.projet.Library.dto.AuthorDTO;
import supnum.projet.Library.services.AuthorService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/authors")
public class AuthorController {
    private final AuthorService service;

    public AuthorController(AuthorService service) {
        this.service = service;
    }

    @GetMapping
    public List<Author> getAll() {
        return service.findAll();
    }

    @PostMapping
    public ResponseEntity<Author> create(@Valid @RequestBody AuthorDTO dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
