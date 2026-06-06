package supnum.projet.Library.controllers;

import supnum.projet.Library.dto.PublisherDTO;
import supnum.projet.Library.dto.response.PublisherResponse;
import supnum.projet.Library.services.PublisherService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/publishers")
public class PublisherController {
    private final PublisherService service;

    public PublisherController(PublisherService service) {
        this.service = service;
    }

    @GetMapping
    public Page<PublisherResponse> getAll(Pageable pageable) {
        return service.findAll(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PublisherResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<PublisherResponse> create(@Valid @RequestBody PublisherDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PublisherResponse> update(@PathVariable Long id, @Valid @RequestBody PublisherDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
