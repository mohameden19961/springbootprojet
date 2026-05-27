package supnum.projet.Library.dao;

import supnum.projet.Library.data.entities.Category;
import supnum.projet.Library.data.repositories.CategoryRepository;
import supnum.projet.Library.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CategoryDao {

    private final CategoryRepository repository;

    public CategoryDao(CategoryRepository repository) {
        this.repository = repository;
    }

    public List<Category> findAll() {
        return repository.findAll();
    }

    public Category findById(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Catégorie non trouvée avec l'id : " + id));
    }

    public Category save(Category category) {
        return repository.save(category);
    }
}
