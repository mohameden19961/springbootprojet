package supnum.projet.Library.data.entities;

import jakarta.persistence.*;
import lombok.*;
import supnum.projet.Library.data.enums.BookItemStatus;

@Entity
@Table(name = "book_item")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookItem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String barcode;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookItemStatus status = BookItemStatus.AVAILABLE;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;
    
    @OneToOne(mappedBy = "bookItem", fetch = FetchType.LAZY)
    private Borrow borrow;
    
    @Version
    private Long version;
    
    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
    private Boolean deleted = false;
}
