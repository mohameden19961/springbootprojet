package supnum.projet.Library.services;

import supnum.projet.Library.data.entities.Nationality;
import supnum.projet.Library.data.repositories.NationalityRepository;
import supnum.projet.Library.exceptions.ResourceNotFoundException;
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

    public List<Nationality> findAll() {
        return repository.findAll();
    }

    public Nationality findById(String code) {
        return repository.findById(code)
            .orElseThrow(() -> new ResourceNotFoundException("Nationalité non trouvée avec le code : " + code));
    }

    public Nationality save(Nationality nationality) {
        return repository.save(nationality);
    }

    public List<Nationality> saveAll(List<Nationality> nationalities) {
        return repository.saveAll(nationalities);
    }
}
