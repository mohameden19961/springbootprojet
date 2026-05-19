package supnum.projet.Library.data.repositories;

import supnum.projet.Library.data.entities.Book;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findByIsbn(String isbn);

    @EntityGraph(attributePaths = {"language", "category", "publisher"})
    @Override
    List<Book> findAll();
}
