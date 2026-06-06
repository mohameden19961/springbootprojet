package supnum.projet.Library.services;

import supnum.projet.Library.data.entities.Category;
import supnum.projet.Library.dto.CategoryDTO;
import supnum.projet.Library.dto.response.CategoryResponse;
import supnum.projet.Library.data.repositories.CategoryRepository;
import supnum.projet.Library.exceptions.DuplicateResourceException;
import supnum.projet.Library.exceptions.ResourceNotFoundException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CategoryService {
    private final CategoryRepository repository;

    public CategoryService(CategoryRepository repository) {
        this.repository = repository;
    }

    @Cacheable(value = "categories", key = "'all_' + #pageable.pageNumber + '_' + #pageable.pageSize")
    public Page<CategoryResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(this::toResponse);
    }

    @CacheEvict(value = "categories", allEntries = true)
    public CategoryResponse create(CategoryDTO dto) {
        if(repository.findByName(dto.getName()).isPresent()) {
            throw new DuplicateResourceException("Une catégorie avec ce nom existe déjà");
        }
        Category cat = Category.builder()
            .name(dto.getName())
            .build();
        return toResponse(repository.save(cat));
    }

    @Cacheable(value = "categories", key = "'byId_' + #id")
    public CategoryResponse findById(Long id) {
        return repository.findById(id)
            .map(this::toResponse)
            .orElseThrow(() -> new ResourceNotFoundException("Catégorie non trouvée avec l'id : " + id));
    }

    @CacheEvict(value = "categories", allEntries = true)
    public CategoryResponse update(Long id, CategoryDTO dto) {
        Category cat = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Catégorie non trouvée avec l'id : " + id));
        if (!cat.getName().equals(dto.getName()) && repository.findByName(dto.getName()).isPresent()) {
            throw new DuplicateResourceException("Une catégorie avec ce nom existe déjà");
        }
        cat.setName(dto.getName());
        return toResponse(repository.save(cat));
    }

    @CacheEvict(value = "categories", allEntries = true)
    public void delete(Long id) {
        Category cat = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Catégorie non trouvée avec l'id : " + id));
        cat.setDeleted(true);
        repository.save(cat);
    }

    private CategoryResponse toResponse(Category cat) {
        CategoryResponse r = new CategoryResponse();
        r.setId(cat.getId());
        r.setName(cat.getName());
        return r;
    }
}
