package supnum.projet.Library.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import supnum.projet.Library.dto.PublisherDTO;
import supnum.projet.Library.services.PublisherService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/publishers")
@RequiredArgsConstructor
@Validated
public class PublisherController {
    
    private final PublisherService publisherService;
    
    @PostMapping
    public ResponseEntity<PublisherDTO> createPublisher(@Valid @RequestBody PublisherDTO publisherDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(publisherService.createPublisher(publisherDTO));
    }
    
    @GetMapping
    public ResponseEntity<Page<PublisherDTO>> getAllPublishers(Pageable pageable) {
        return ResponseEntity.ok(publisherService.getAllPublishers(pageable));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<PublisherDTO> getPublisherById(@PathVariable Long id) {
        return ResponseEntity.ok(publisherService.getPublisherById(id));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<PublisherDTO> updatePublisher(
            @PathVariable Long id,
            @Valid @RequestBody PublisherDTO publisherDTO) {
        return ResponseEntity.ok(publisherService.updatePublisher(id, publisherDTO));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePublisher(@PathVariable Long id) {
        publisherService.deletePublisher(id);
        return ResponseEntity.noContent().build();
    }
}
