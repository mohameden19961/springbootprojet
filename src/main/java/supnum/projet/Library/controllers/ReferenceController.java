package supnum.projet.Library.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import supnum.projet.Library.data.repositories.LanguageRepository;
import supnum.projet.Library.data.repositories.NationalityRepository;
import supnum.projet.Library.dto.LanguageDTO;
import supnum.projet.Library.dto.NationalityDTO;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/reference")
@RequiredArgsConstructor
public class ReferenceController {
    
    private final LanguageRepository languageRepository;
    private final NationalityRepository nationalityRepository;
    
    // ============ LANGUAGES ============
    
    @GetMapping("/languages")
    public ResponseEntity<List<LanguageDTO>> getAllLanguages() {
        List<LanguageDTO> languages = languageRepository.findAll().stream()
                .map(l -> new LanguageDTO(l.getCode(), l.getName()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(languages);
    }
    
    @GetMapping("/languages/{code}")
    public ResponseEntity<LanguageDTO> getLanguage(@PathVariable String code) {
        return languageRepository.findById(code)
                .map(l -> ResponseEntity.ok(new LanguageDTO(l.getCode(), l.getName())))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    @PostMapping("/languages")
    public ResponseEntity<LanguageDTO> createLanguage(@RequestBody LanguageDTO languageDTO) {
        var language = languageRepository.save(new supnum.projet.Library.data.entities.Language(
                languageDTO.getCode(),
                languageDTO.getName()
        ));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new LanguageDTO(language.getCode(), language.getName()));
    }
    
    // ============ NATIONALITIES ============
    
    @GetMapping("/nationalities")
    public ResponseEntity<List<NationalityDTO>> getAllNationalities() {
        List<NationalityDTO> nationalities = nationalityRepository.findAll().stream()
                .map(n -> new NationalityDTO(n.getCode(), n.getName()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(nationalities);
    }
    
    @GetMapping("/nationalities/{code}")
    public ResponseEntity<NationalityDTO> getNationality(@PathVariable String code) {
        return nationalityRepository.findById(code)
                .map(n -> ResponseEntity.ok(new NationalityDTO(n.getCode(), n.getName())))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    @PostMapping("/nationalities")
    public ResponseEntity<NationalityDTO> createNationality(@RequestBody NationalityDTO nationalityDTO) {
        var nationality = nationalityRepository.save(new supnum.projet.Library.data.entities.Nationality(
                nationalityDTO.getCode(),
                nationalityDTO.getName()
        ));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new NationalityDTO(nationality.getCode(), nationality.getName()));
    }
}
