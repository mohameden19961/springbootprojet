package supnum.projet.Library.controllers;

import supnum.projet.Library.data.entities.Language;
import supnum.projet.Library.data.repositories.LanguageRepository;
import supnum.projet.Library.exceptions.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/languages")
public class LanguageController {

    private final LanguageRepository repository;

    public LanguageController(LanguageRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Language> getAll() {
        return repository.findAll();
    }

    @GetMapping("/{code}")
    public ResponseEntity<Language> getByCode(@PathVariable String code) {
        return ResponseEntity.ok(repository.findById(code)
            .orElseThrow(() -> new ResourceNotFoundException("Langue non trouvée avec le code : " + code)));
    }
}
