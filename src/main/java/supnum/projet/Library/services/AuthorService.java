package supnum.projet.Library.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import supnum.projet.Library.data.entities.Author;
import supnum.projet.Library.data.entities.Nationality;
import supnum.projet.Library.data.repositories.AuthorRepository;
import supnum.projet.Library.data.repositories.NationalityRepository;
import supnum.projet.Library.dto.AuthorDTO;
import supnum.projet.Library.dto.NationalityDTO;
import supnum.projet.Library.exceptions.ResourceNotFoundException;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthorService {
    
    private final AuthorRepository authorRepository;
    private final NationalityRepository nationalityRepository;
    
    public AuthorDTO createAuthor(AuthorDTO authorDTO) {
        Nationality nationality = null;
        if (authorDTO.getNationalityCode() != null) {
            nationality = nationalityRepository.findById(authorDTO.getNationalityCode())
                    .orElseThrow(() -> new ResourceNotFoundException("Nationality not found with code: " + authorDTO.getNationalityCode()));
        }
        
        Author author = Author.builder()
                .name(authorDTO.getName())
                .nationality(nationality)
                .deleted(false)
                .build();
        
        Author saved = authorRepository.save(author);
        return mapToDTO(saved);
    }
    
    @Transactional(readOnly = true)
    public Page<AuthorDTO> getAllAuthors(Pageable pageable) {
        return authorRepository.findAllActive(pageable)
                .map(this::mapToDTO);
    }
    
    @Transactional(readOnly = true)
    public Page<AuthorDTO> searchAuthors(String name, Pageable pageable) {
        return authorRepository.findByNameContainingActive(name, pageable)
                .map(this::mapToDTO);
    }
    
    @Transactional(readOnly = true)
    public AuthorDTO getAuthorById(Long id) {
        Author author = authorRepository.findById(id)
                .filter(a -> !a.getDeleted())
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with id: " + id));
        return mapToDTO(author);
    }
    
    public AuthorDTO updateAuthor(Long id, AuthorDTO authorDTO) {
        Author author = authorRepository.findById(id)
                .filter(a -> !a.getDeleted())
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with id: " + id));
        
        Nationality nationality = null;
        if (authorDTO.getNationalityCode() != null) {
            nationality = nationalityRepository.findById(authorDTO.getNationalityCode())
                    .orElseThrow(() -> new ResourceNotFoundException("Nationality not found with code: " + authorDTO.getNationalityCode()));
        }
        
        author.setName(authorDTO.getName());
        author.setNationality(nationality);
        Author updated = authorRepository.save(author);
        return mapToDTO(updated);
    }
    
    public void deleteAuthor(Long id) {
        Author author = authorRepository.findById(id)
                .filter(a -> !a.getDeleted())
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with id: " + id));
        
        author.setDeleted(true);
        authorRepository.save(author);
    }
    
    private AuthorDTO mapToDTO(Author author) {
        return AuthorDTO.builder()
                .id(author.getId())
                .name(author.getName())
                .nationalityCode(author.getNationality() != null ? author.getNationality().getCode() : null)
                .nationality(author.getNationality() != null ? new NationalityDTO(author.getNationality().getCode(), author.getNationality().getName()) : null)
                .build();
    }
}
