package supnum.projet.Library.services;

import supnum.projet.Library.data.entities.Publisher;
import supnum.projet.Library.dto.PublisherDTO;
import supnum.projet.Library.data.repositories.PublisherRepository;
import supnum.projet.Library.exceptions.DuplicateResourceException;
import supnum.projet.Library.exceptions.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PublisherService {
    private final PublisherRepository repository;

    public PublisherService(PublisherRepository repository) {
        this.repository = repository;
    }

    public Page<Publisher> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Publisher create(PublisherDTO dto) {
        if (repository.findByName(dto.getName()).isPresent()) {
            throw new DuplicateResourceException("Un éditeur avec ce nom existe déjà");
        }
        Publisher publisher = Publisher.builder()
            .name(dto.getName())
            .email(dto.getEmail())
            .build();
        return repository.save(publisher);
    }

    public Publisher findById(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Éditeur non trouvé avec l'id : " + id));
    }

    public Publisher update(Long id, PublisherDTO dto) {
        Publisher publisher = findById(id);
        if (!publisher.getName().equals(dto.getName()) && repository.findByName(dto.getName()).isPresent()) {
            throw new DuplicateResourceException("Un éditeur avec ce nom existe déjà");
        }
        publisher.setName(dto.getName());
        publisher.setEmail(dto.getEmail());
        return repository.save(publisher);
    }

    public void delete(Long id) {
        Publisher publisher = findById(id);
        publisher.setDeleted(true);
        repository.save(publisher);
    }
}
