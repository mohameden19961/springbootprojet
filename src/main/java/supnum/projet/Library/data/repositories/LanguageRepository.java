package supnum.projet.Library.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import supnum.projet.Library.data.entities.Language;

@Repository
public interface LanguageRepository extends JpaRepository<Language, String> {
}
