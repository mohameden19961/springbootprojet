package supnum.projet.Library.dao;

import supnum.projet.Library.data.entities.Nationality;
import supnum.projet.Library.data.repositories.NationalityRepository;
import supnum.projet.Library.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class NationalityDao {

    private final NationalityRepository repository;

    public NationalityDao(NationalityRepository repository) {
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
}
