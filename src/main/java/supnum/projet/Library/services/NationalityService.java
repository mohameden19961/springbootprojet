package supnum.projet.Library.services;

import supnum.projet.Library.data.entities.Nationality;
import supnum.projet.Library.data.repositories.NationalityRepository;
import supnum.projet.Library.exceptions.ResourceNotFoundException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class NationalityService {

    private final NationalityRepository repository;

    public NationalityService(NationalityRepository repository) {
        this.repository = repository;
    }

    @Cacheable("nationalities")
    public List<Nationality> findAll() {
        return repository.findAll();
    }

    @Cacheable(value = "nationalities", key = "'byCode_' + #code")
    public Nationality findById(String code) {
        return repository.findById(code)
            .orElseThrow(() -> new ResourceNotFoundException("Nationalité non trouvée avec le code : " + code));
    }

    @CacheEvict(value = "nationalities", allEntries = true)
    public Nationality save(Nationality nationality) {
        return repository.save(nationality);
    }

    @CacheEvict(value = "nationalities", allEntries = true)
    public List<Nationality> saveAll(List<Nationality> nationalities) {
        return repository.saveAll(nationalities);
    }
}
