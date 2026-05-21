package supnum.projet.Library.dto;

import supnum.projet.Library.data.entities.Book;
import supnum.projet.Library.data.entities.Category;
import supnum.projet.Library.data.entities.Language;
import supnum.projet.Library.data.entities.Publisher;

public class BookResponseDTO {
    private Long id;
    private String title;
    private String isbn;
    private String languageCode;
    private String languageName;
    private Long categoryId;
    private String categoryName;
    private Long publisherId;
    private String publisherName;

    public BookResponseDTO() {}

    public BookResponseDTO(Long id, String title, String isbn,
                           String languageCode, String languageName,
                           Long categoryId, String categoryName,
                           Long publisherId, String publisherName) {
        this.id = id;
        this.title = title;
        this.isbn = isbn;
        this.languageCode = languageCode;
        this.languageName = languageName;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.publisherId = publisherId;
        this.publisherName = publisherName;
    }

    public static BookResponseDTO from(Book entity) {
        if (entity == null) return null;
        Language lang = entity.getLanguage();
        Category cat = entity.getCategory();
        Publisher pub = entity.getPublisher();
        return new BookResponseDTO(
            entity.getId(),
            entity.getTitle(),
            entity.getIsbn(),
            lang != null ? lang.getCode() : null,
            lang != null ? lang.getName() : null,
            cat != null ? cat.getId() : null,
            cat != null ? cat.getName() : null,
            pub != null ? pub.getId() : null,
            pub != null ? pub.getName() : null
        );
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public String getLanguageCode() { return languageCode; }
    public void setLanguageCode(String c) { this.languageCode = c; }
    public String getLanguageName() { return languageName; }
    public void setLanguageName(String n) { this.languageName = n; }
    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long id) { this.categoryId = id; }
    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String n) { this.categoryName = n; }
    public Long getPublisherId() { return publisherId; }
    public void setPublisherId(Long id) { this.publisherId = id; }
    public String getPublisherName() { return publisherName; }
    public void setPublisherName(String n) { this.publisherName = n; }
}
