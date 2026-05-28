package supnum.projet.Library.data.entities;

import supnum.projet.Library.data.entities.enums.AuthorRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import java.io.Serializable;

@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "book_author")
public class BookAuthor {

    @EmbeddedId
    @Builder.Default
    private BookAuthorId id = new BookAuthorId();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("bookId")
    @JoinColumn(name = "book_id")
    @NotFound(action = NotFoundAction.IGNORE)
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("authorId")
    @JoinColumn(name = "author_id")
    @NotFound(action = NotFoundAction.IGNORE)
    private Author author;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AuthorRole role;

    @Getter @Setter
    @EqualsAndHashCode
    @AllArgsConstructor
    @NoArgsConstructor
    @Embeddable
    public static class BookAuthorId implements Serializable {
        private Long bookId;
        private Long authorId;
    }
}
