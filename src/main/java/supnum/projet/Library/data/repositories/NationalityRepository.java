package supnum.projet.Library.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import supnum.projet.Library.data.entities.Nationality;

@Repository
public interface NationalityRepository extends JpaRepository<Nationality, String> {
}
