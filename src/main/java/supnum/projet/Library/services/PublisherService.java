package supnum.projet.Library.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import supnum.projet.Library.data.entities.Publisher;
import supnum.projet.Library.data.repositories.PublisherRepository;
import supnum.projet.Library.dto.PublisherDTO;
import supnum.projet.Library.exceptions.ResourceNotFoundException;
import supnum.projet.Library.exceptions.DuplicateResourceException;

@Service
@RequiredArgsConstructor
@Transactional
public class PublisherService {
    
    private final PublisherRepository publisherRepository;
    
    public PublisherDTO createPublisher(PublisherDTO publisherDTO) {
        if (publisherRepository.findByNameActive(publisherDTO.getName()) != null) {
            throw new DuplicateResourceException("Publisher with name '" + publisherDTO.getName() + "' already exists");
        }
        
        if (publisherRepository.findByEmailActive(publisherDTO.getEmail()) != null) {
            throw new DuplicateResourceException("Publisher with email '" + publisherDTO.getEmail() + "' already exists");
        }
        
        Publisher publisher = Publisher.builder()
                .name(publisherDTO.getName())
                .email(publisherDTO.getEmail())
                .deleted(false)
                .build();
        
        Publisher saved = publisherRepository.save(publisher);
        return mapToDTO(saved);
    }
    
    @Transactional(readOnly = true)
    public Page<PublisherDTO> getAllPublishers(Pageable pageable) {
        return publisherRepository.findAllActive(pageable)
                .map(this::mapToDTO);
    }
    
    @Transactional(readOnly = true)
    public PublisherDTO getPublisherById(Long id) {
        Publisher publisher = publisherRepository.findById(id)
                .filter(p -> !p.getDeleted())
                .orElseThrow(() -> new ResourceNotFoundException("Publisher not found with id: " + id));
        return mapToDTO(publisher);
    }
    
    public PublisherDTO updatePublisher(Long id, PublisherDTO publisherDTO) {
        Publisher publisher = publisherRepository.findById(id)
                .filter(p -> !p.getDeleted())
                .orElseThrow(() -> new ResourceNotFoundException("Publisher not found with id: " + id));
        
        if (!publisher.getName().equals(publisherDTO.getName()) && 
            publisherRepository.findByNameActive(publisherDTO.getName()) != null) {
            throw new DuplicateResourceException("Publisher with name '" + publisherDTO.getName() + "' already exists");
        }
        
        if (!publisher.getEmail().equals(publisherDTO.getEmail()) && 
            publisherRepository.findByEmailActive(publisherDTO.getEmail()) != null) {
            throw new DuplicateResourceException("Publisher with email '" + publisherDTO.getEmail() + "' already exists");
        }
        
        publisher.setName(publisherDTO.getName());
        publisher.setEmail(publisherDTO.getEmail());
        Publisher updated = publisherRepository.save(publisher);
        return mapToDTO(updated);
    }
    
    public void deletePublisher(Long id) {
        Publisher publisher = publisherRepository.findById(id)
                .filter(p -> !p.getDeleted())
                .orElseThrow(() -> new ResourceNotFoundException("Publisher not found with id: " + id));
        
        publisher.setDeleted(true);
        publisherRepository.save(publisher);
    }
    
    private PublisherDTO mapToDTO(Publisher publisher) {
        return PublisherDTO.builder()
                .id(publisher.getId())
                .name(publisher.getName())
                .email(publisher.getEmail())
                .build();
    }
}
