package supnum.projet.Library.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import supnum.projet.Library.data.entities.BookAuthor;
import supnum.projet.Library.data.entities.BookAuthorId;

@Repository
public interface BookAuthorRepository extends JpaRepository<BookAuthor, BookAuthorId> {
}
