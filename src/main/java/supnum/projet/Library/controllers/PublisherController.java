package supnum.projet.Library.controllers;

import supnum.projet.Library.dto.PublisherDTO;
import supnum.projet.Library.dto.PublisherResponseDTO;
import supnum.projet.Library.services.PublisherService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/publishers")
public class PublisherController {
    private final PublisherService service;

    public PublisherController(PublisherService service) {
        this.service = service;
    }

    @GetMapping
    public List<PublisherResponseDTO> getAll() {
        return service.findAll().stream().map(PublisherResponseDTO::from).toList();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PublisherResponseDTO> create(@Valid @RequestBody PublisherDTO dto) {
        return ResponseEntity.ok(PublisherResponseDTO.from(service.create(dto)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
