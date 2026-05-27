package supnum.projet.Library.services;

import supnum.projet.Library.data.entities.Language;
import supnum.projet.Library.data.repositories.LanguageRepository;
import supnum.projet.Library.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class LanguageService {

    private final LanguageRepository repository;

    public LanguageService(LanguageRepository repository) {
        this.repository = repository;
    }

    public List<Language> findAll() {
        return repository.findAll();
    }

    public Language findById(String code) {
        return repository.findById(code)
            .orElseThrow(() -> new ResourceNotFoundException("Langue non trouvée avec le code : " + code));
    }

    public Language save(Language language) {
        return repository.save(language);
    }

    public List<Language> saveAll(List<Language> languages) {
        return repository.saveAll(languages);
    }
}
