package supnum.projet.Library.data.repositories;

import supnum.projet.Library.data.entities.BookAuthor;
import supnum.projet.Library.data.entities.BookAuthor.BookAuthorId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookAuthorRepository extends JpaRepository<BookAuthor, BookAuthorId> {

    @Query("SELECT ba FROM BookAuthor ba WHERE ba.book.id = :bookId")
    List<BookAuthor> findByBookId(@Param("bookId") Long bookId);
}
