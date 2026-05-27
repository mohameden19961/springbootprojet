package supnum.projet.Library.controllers;

import supnum.projet.Library.data.entities.Language;
import supnum.projet.Library.services.LanguageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/languages")
public class LanguageController {

    private final LanguageService service;

    public LanguageController(LanguageService service) {
        this.service = service;
    }

    @GetMapping
    public List<Language> getAll() {
        return service.findAll();
    }

    @GetMapping("/{code}")
    public ResponseEntity<Language> getByCode(@PathVariable String code) {
        return ResponseEntity.ok(service.findById(code));
    }
}
