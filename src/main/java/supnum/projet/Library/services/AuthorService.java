package supnum.projet.Library.services;

import supnum.projet.Library.data.entities.Author;
import supnum.projet.Library.data.entities.Nationality;
import supnum.projet.Library.dto.AuthorDTO;
import supnum.projet.Library.data.repositories.AuthorRepository;
import supnum.projet.Library.data.repositories.NationalityRepository;
import supnum.projet.Library.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AuthorService {
    private final AuthorRepository repository;
    private final NationalityRepository nationalityRepository;

    public AuthorService(AuthorRepository repository, NationalityRepository nationalityRepository) {
        this.repository = repository;
        this.nationalityRepository = nationalityRepository;
    }

    public List<Author> findAll() {
        return repository.findAll();
    }

    public Author create(AuthorDTO dto) {
        Nationality nat = nationalityRepository.findById(dto.getNationalityCode())
            .orElseThrow(() -> new ResourceNotFoundException("Nationalité introuvable"));
        Author author = new Author();
        author.setName(dto.getName());
        author.setNationality(nat);
        return repository.save(author);
    }

    public void delete(Long id) {
        Author author = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Auteur non trouvé"));
        author.setDeleted(true);
        repository.save(author);
    }
}
