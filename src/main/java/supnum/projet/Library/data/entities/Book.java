package supnum.projet.Library.data.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.SQLRestriction;
import java.util.List;

@Entity
@Table(name = "book")
@SQLRestriction("deleted = false")
public class Book extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, unique = true, length = 20)
    private String isbn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "language_code")
    private Language language;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    @NotFound(action = NotFoundAction.IGNORE)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "publisher_id")
    @NotFound(action = NotFoundAction.IGNORE)
    private Publisher publisher;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BookItem> bookItems;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public Language getLanguage() { return language; }
    public void setLanguage(Language language) { this.language = language; }
    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }
    public Publisher getPublisher() { return publisher; }
    public void setPublisher(Publisher publisher) { this.publisher = publisher; }
    public List<BookItem> getBookItems() { return bookItems; }
    public void setBookItems(List<BookItem> bookItems) { this.bookItems = bookItems; }
}
