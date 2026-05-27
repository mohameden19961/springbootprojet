package supnum.projet.Library.dao;

import supnum.projet.Library.data.entities.Language;
import supnum.projet.Library.data.repositories.LanguageRepository;
import supnum.projet.Library.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class LanguageDao {

    private final LanguageRepository repository;

    public LanguageDao(LanguageRepository repository) {
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
}
