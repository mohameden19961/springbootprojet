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
    private final AuthorRepository authorRepository;
    private final NationalityRepository nationalityRepository;

    public AuthorService(AuthorRepository authorRepository, NationalityRepository nationalityRepository) {
        this.authorRepository = authorRepository;
        this.nationalityRepository = nationalityRepository;
    }

    public List<Author> findAll() {
        return authorRepository.findAll();
    }

    public Author create(AuthorDTO dto) {
        Nationality nationality = nationalityRepository.findById(dto.getNationalityCode())
            .orElseThrow(() -> new ResourceNotFoundException("Nationalité non trouvée avec le code : " + dto.getNationalityCode()));

        Author author = new Author();
        author.setName(dto.getName());
        author.setNationality(nationality);
        
        return authorRepository.save(author);
    }

    public void delete(Long id) {
        Author author = authorRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Auteur non trouvé avec l'id : " + id));
        author.setDeleted(true);
        authorRepository.save(author);
    }
}