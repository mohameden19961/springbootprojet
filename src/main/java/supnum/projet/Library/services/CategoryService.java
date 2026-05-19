package supnum.projet.Library.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import supnum.projet.Library.data.entities.Category;
import supnum.projet.Library.data.repositories.CategoryRepository;
import supnum.projet.Library.dto.CategoryDTO;
import supnum.projet.Library.exceptions.ResourceNotFoundException;
import supnum.projet.Library.exceptions.DuplicateResourceException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {
    
    private final CategoryRepository categoryRepository;
    
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        if (categoryRepository.findByNameActive(categoryDTO.getName()) != null) {
            throw new DuplicateResourceException("Category with name '" + categoryDTO.getName() + "' already exists");
        }
        
        Category category = Category.builder()
                .name(categoryDTO.getName())
                .deleted(false)
                .build();
        
        Category saved = categoryRepository.save(category);
        return mapToDTO(saved);
    }
    
    @Transactional(readOnly = true)
    public Page<CategoryDTO> getAllCategories(Pageable pageable) {
        return categoryRepository.findAllActive(pageable)
                .map(this::mapToDTO);
    }
    
    @Transactional(readOnly = true)
    public CategoryDTO getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .filter(c -> !c.getDeleted())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
        return mapToDTO(category);
    }
    
    public CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO) {
        Category category = categoryRepository.findById(id)
                .filter(c -> !c.getDeleted())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
        
        if (!category.getName().equals(categoryDTO.getName()) && 
            categoryRepository.findByNameActive(categoryDTO.getName()) != null) {
            throw new DuplicateResourceException("Category with name '" + categoryDTO.getName() + "' already exists");
        }
        
        category.setName(categoryDTO.getName());
        Category updated = categoryRepository.save(category);
        return mapToDTO(updated);
    }
    
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .filter(c -> !c.getDeleted())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
        
        category.setDeleted(true);
        categoryRepository.save(category);
    }
    
    private CategoryDTO mapToDTO(Category category) {
        return CategoryDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }
}
