package supnum.projet.Library.services;

import supnum.projet.Library.data.entities.Author;
import supnum.projet.Library.data.entities.Nationality;
import supnum.projet.Library.dto.AuthorDTO;
import supnum.projet.Library.dto.response.AuthorResponse;
import supnum.projet.Library.data.repositories.AuthorRepository;
import supnum.projet.Library.data.repositories.NationalityRepository;
import supnum.projet.Library.exceptions.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthorService {
    private final AuthorRepository authorRepository;
    private final NationalityRepository nationalityRepository;

    public AuthorService(AuthorRepository authorRepository, NationalityRepository nationalityRepository) {
        this.authorRepository = authorRepository;
        this.nationalityRepository = nationalityRepository;
    }

    public Page<AuthorResponse> findAll(Pageable pageable) {
        return authorRepository.findAll(pageable).map(this::toResponse);
    }

    public AuthorResponse create(AuthorDTO dto) {
        Nationality nationality = nationalityRepository.findById(dto.getNationalityCode())
            .orElseThrow(() -> new ResourceNotFoundException("Nationalité non trouvée avec le code : " + dto.getNationalityCode()));
        Author author = Author.builder()
            .name(dto.getName())
            .nationality(nationality)
            .build();

        return toResponse(authorRepository.save(author));
    }

    public AuthorResponse findById(Long id) {
        return authorRepository.findById(id)
            .map(this::toResponse)
            .orElseThrow(() -> new ResourceNotFoundException("Auteur non trouvé avec l'id : " + id));
    }

    public AuthorResponse update(Long id, AuthorDTO dto) {
        Author author = authorRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Auteur non trouvé avec l'id : " + id));
        Nationality nationality = nationalityRepository.findById(dto.getNationalityCode())
            .orElseThrow(() -> new ResourceNotFoundException("Nationalité non trouvée avec le code : " + dto.getNationalityCode()));
        author.setName(dto.getName());
        author.setNationality(nationality);
        return toResponse(authorRepository.save(author));
    }

    public void delete(Long id) {
        Author author = authorRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Auteur non trouvé avec l'id : " + id));
        author.setDeleted(true);
        authorRepository.save(author);
    }

    private AuthorResponse toResponse(Author author) {
        AuthorResponse r = new AuthorResponse();
        r.setId(author.getId());
        r.setName(author.getName());
        if (author.getNationality() != null) {
            r.setNationalityCode(author.getNationality().getCode());
            r.setNationalityName(author.getNationality().getName());
        }
        return r;
    }
}
