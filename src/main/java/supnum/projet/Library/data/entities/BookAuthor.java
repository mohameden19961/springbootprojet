package supnum.projet.Library.data.entities;

import supnum.projet.Library.data.entities.enums.AuthorRole;
import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "book_author")
public class BookAuthor {

    @EmbeddedId
    private BookAuthorId id = new BookAuthorId();

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

    public BookAuthorId getId() { return id; }
    public void setId(BookAuthorId id) { this.id = id; }
    public Book getBook() { return book; }
    public void setBook(Book book) { this.book = book; }
    public Author getAuthor() { return author; }
    public void setAuthor(Author author) { this.author = author; }
    public AuthorRole getRole() { return role; }
    public void setRole(AuthorRole role) { this.role = role; }

    @Embeddable
    public static class BookAuthorId implements Serializable {
        private Long bookId;
        private Long authorId;

        public Long getBookId() { return bookId; }
        public void setBookId(Long bookId) { this.bookId = bookId; }
        public Long getAuthorId() { return authorId; }
        public void setAuthorId(Long authorId) { this.authorId = authorId; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            BookAuthorId that = (BookAuthorId) o;
            return java.util.Objects.equals(bookId, that.bookId) && java.util.Objects.equals(authorId, that.authorId);
        }

        @Override
        public int hashCode() {
            return java.util.Objects.hash(bookId, authorId);
        }
    }
}
