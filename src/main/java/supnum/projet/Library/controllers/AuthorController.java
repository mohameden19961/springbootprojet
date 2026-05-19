package supnum.projet.Library.controllers;

import supnum.projet.Library.dto.AuthorDTO;
import supnum.projet.Library.dto.AuthorResponseDTO;
import supnum.projet.Library.services.AuthorService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public List<AuthorResponseDTO> getAll() {
        return service.findAll().stream().map(AuthorResponseDTO::from).toList();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AuthorResponseDTO> create(@Valid @RequestBody AuthorDTO dto) {
        return ResponseEntity.ok(AuthorResponseDTO.from(service.create(dto)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
