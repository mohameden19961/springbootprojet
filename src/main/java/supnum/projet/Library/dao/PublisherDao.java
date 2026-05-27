package supnum.projet.Library.dao;

import supnum.projet.Library.data.entities.Publisher;
import supnum.projet.Library.data.repositories.PublisherRepository;
import supnum.projet.Library.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PublisherDao {

    private final PublisherRepository repository;

    public PublisherDao(PublisherRepository repository) {
        this.repository = repository;
    }

    public List<Publisher> findAll() {
        return repository.findAll();
    }

    public Publisher findById(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Éditeur non trouvé avec l'id : " + id));
    }

    public Publisher save(Publisher publisher) {
        return repository.save(publisher);
    }
}
