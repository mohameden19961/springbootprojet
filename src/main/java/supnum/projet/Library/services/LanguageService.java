package supnum.projet.Library.services;

import supnum.projet.Library.data.entities.Language;
import supnum.projet.Library.data.repositories.LanguageRepository;
import supnum.projet.Library.exceptions.ResourceNotFoundException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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

    @Cacheable("languages")
    public List<Language> findAll() {
        return repository.findAll();
    }

    @Cacheable(value = "languages", key = "'byCode_' + #code")
    public Language findById(String code) {
        return repository.findById(code)
            .orElseThrow(() -> new ResourceNotFoundException("Langue non trouvée avec le code : " + code));
    }

    @CacheEvict(value = "languages", allEntries = true)
    public Language save(Language language) {
        return repository.save(language);
    }

    @CacheEvict(value = "languages", allEntries = true)
    public List<Language> saveAll(List<Language> languages) {
        return repository.saveAll(languages);
    }
}
