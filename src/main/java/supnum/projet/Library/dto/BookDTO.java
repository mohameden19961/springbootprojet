package supnum.projet.Library.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class BookDTO {
    private Long id;

    @NotBlank(message = "Le titre est requis")
    private String title;

    @NotBlank(message = "L'ISBN est requis")
    private String isbn;

    @NotBlank(message = "Le code de langue est requis")
    private String languageCode;

    @NotNull(message = "La catégorie est requise")
    private Long categoryId;

    @NotNull(message = "L'éditeur est requis")
    private Long publisherId;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public String getLanguageCode() { return languageCode; }
    public void setLanguageCode(String languageCode) { this.languageCode = languageCode; }
    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
    public Long getPublisherId() { return publisherId; }
    public void setPublisherId(Long publisherId) { this.publisherId = publisherId; }
}
