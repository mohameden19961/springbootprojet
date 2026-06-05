package supnum.projet.Library.controllers;

import supnum.projet.Library.data.entities.Nationality;
import supnum.projet.Library.services.NationalityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/nationalities")
public class NationalityController {

    private final NationalityService service;

    public NationalityController(NationalityService service) {
        this.service = service;
    }

    @GetMapping
    public List<Nationality> getAll() {
        return service.findAll();
    }

    @GetMapping("/{code}")
    public ResponseEntity<Nationality> getByCode(@PathVariable String code) {
        return ResponseEntity.ok(service.findById(code));
    }
}
