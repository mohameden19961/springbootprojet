package supnum.projet.Library.data.entities;

import supnum.projet.Library.data.entities.enums.BookItemStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Getter @Setter
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
}
