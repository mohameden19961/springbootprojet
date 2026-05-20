package supnum.projet.Library.data.repositories;

import supnum.projet.Library.data.entities.BookAuthor;
import supnum.projet.Library.data.entities.BookAuthor.BookAuthorId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookAuthorRepository extends JpaRepository<BookAuthor, BookAuthorId> {
}
