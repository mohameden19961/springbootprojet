package supnum.projet.Library.controllers;

import supnum.projet.Library.dto.CategoryDTO;
import supnum.projet.Library.dto.CategoryResponseDTO;
import supnum.projet.Library.services.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryService service;

    public CategoryController(CategoryService service) {
        this.service = service;
    }

    @GetMapping
    public List<CategoryResponseDTO> getAll() {
        return service.findAll().stream().map(CategoryResponseDTO::from).toList();
    }

    @PostMapping
    public ResponseEntity<CategoryResponseDTO> create(@Valid @RequestBody CategoryDTO dto) {
        return ResponseEntity.ok(CategoryResponseDTO.from(service.create(dto)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
