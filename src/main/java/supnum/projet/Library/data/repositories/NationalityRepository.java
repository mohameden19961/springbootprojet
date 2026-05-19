package supnum.projet.Library.data.repositories;

import supnum.projet.Library.data.entities.Nationality;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NationalityRepository extends JpaRepository<Nationality, String> {}