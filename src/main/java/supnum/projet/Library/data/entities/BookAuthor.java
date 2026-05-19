package supnum.projet.Library.data.entities;

import jakarta.persistence.*;
import lombok.*;
import supnum.projet.Library.data.enums.AuthorRole;

@Entity
@Table(name = "book_author")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookAuthor {
    
    @EmbeddedId
    private BookAuthorId id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("bookId")
    @JoinColumn(name = "book_id")
    private Book book;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("authorId")
    @JoinColumn(name = "author_id")
    private Author author;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AuthorRole role;
}
