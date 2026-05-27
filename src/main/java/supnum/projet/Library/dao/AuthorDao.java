package supnum.projet.Library.dao;

import supnum.projet.Library.data.entities.Author;
import supnum.projet.Library.data.repositories.AuthorRepository;
import supnum.projet.Library.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AuthorDao {

    private final AuthorRepository repository;

    public AuthorDao(AuthorRepository repository) {
        this.repository = repository;
    }

    public List<Author> findAll() {
        return repository.findAll();
    }

    public Author findById(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Auteur non trouvé avec l'id : " + id));
    }

    public Author save(Author author) {
        return repository.save(author);
    }

    public void softDelete(Long id) {
        Author author = findById(id);
        author.setDeleted(true);
        repository.save(author);
    }
}
