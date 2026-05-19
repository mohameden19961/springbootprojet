package supnum.projet.Library.services;

import supnum.projet.Library.data.entities.Category;
import supnum.projet.Library.dto.CategoryDTO;
import supnum.projet.Library.data.repositories.CategoryRepository;
import supnum.projet.Library.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CategoryService {
    private final CategoryRepository repository;

    public CategoryService(CategoryRepository repository) {
        this.repository = repository;
    }

    public List<Category> findAll() {
        return repository.findAll();
    }

    public Category create(CategoryDTO dto) {
        if(repository.findByName(dto.getName()).isPresent()) {
            throw new RuntimeException("Une catégorie avec ce nom existe déjà");
        }
        Category cat = new Category();
        cat.setName(dto.getName());
        return repository.save(cat);
    }

    public void delete(Long id) {
        Category cat = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Catégorie non trouvée"));
        cat.setDeleted(true);
        repository.save(cat);
    }
}