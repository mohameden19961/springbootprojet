package supnum.projet.Library.controllers;

import supnum.projet.Library.data.entities.Nationality;
import supnum.projet.Library.data.repositories.NationalityRepository;
import supnum.projet.Library.exceptions.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/nationalities")
public class NationalityController {

    private final NationalityRepository repository;

    public NationalityController(NationalityRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Nationality> getAll() {
        return repository.findAll();
    }

    @GetMapping("/{code}")
    public ResponseEntity<Nationality> getByCode(@PathVariable String code) {
        return ResponseEntity.ok(repository.findById(code)
            .orElseThrow(() -> new ResourceNotFoundException("Nationalité non trouvée avec le code : " + code)));
    }
}
