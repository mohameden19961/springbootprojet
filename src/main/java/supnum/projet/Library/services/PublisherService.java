package supnum.projet.Library.services;

import supnum.projet.Library.data.entities.Publisher;
import supnum.projet.Library.dto.PublisherDTO;
import supnum.projet.Library.data.repositories.PublisherRepository;
import supnum.projet.Library.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class PublisherService {
    private final PublisherRepository repository;

    public PublisherService(PublisherRepository repository) {
        this.repository = repository;
    }

    public List<Publisher> findAll() {
        return repository.findAll();
    }

    public Publisher create(PublisherDTO dto) {
        if (repository.findByName(dto.getName()).isPresent()) {
            throw new RuntimeException("Un éditeur avec ce nom existe déjà");
        }
        Publisher publisher = new Publisher();
        publisher.setName(dto.getName());
        publisher.setEmail(dto.getEmail());
        return repository.save(publisher);
    }

    public void delete(Long id) {
        Publisher publisher = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Éditeur non trouvé avec l'id : " + id));
        publisher.setDeleted(true);
        repository.save(publisher);
    }
}