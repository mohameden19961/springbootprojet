package supnum.projet.Library.dto;

import supnum.projet.Library.data.entities.Category;

public class CategoryResponseDTO {
    private Long id;
    private String name;

    public CategoryResponseDTO() {}

    public CategoryResponseDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static CategoryResponseDTO from(Category entity) {
        if (entity == null) return null;
        return new CategoryResponseDTO(entity.getId(), entity.getName());
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
