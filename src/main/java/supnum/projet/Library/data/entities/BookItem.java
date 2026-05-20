package supnum.projet.Library.data.entities;

import supnum.projet.Library.data.entities.enums.BookItemStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.SQLRestriction;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "book_item")
@SQLRestriction("deleted = false")
public class BookItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String barcode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookItemStatus status = BookItemStatus.AVAILABLE;

    @Version
    private Long version;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getBarcode() { return barcode; }
    public void setBarcode(String barcode) { this.barcode = barcode; }
    public BookItemStatus getStatus() { return status; }
    public void setStatus(BookItemStatus status) { this.status = status; }
    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }
    public Book getBook() { return book; }
    public void setBook(Book book) { this.book = book; }
}
