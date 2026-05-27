package supnum.projet.Library.dao;

import supnum.projet.Library.data.entities.User;
import supnum.projet.Library.data.repositories.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserDao {

    private final UserRepository repository;

    public UserDao(UserRepository repository) {
        this.repository = repository;
    }

    public Optional<User> findByUsername(String username) {
        return repository.findByUsername(username);
    }

    public boolean existsByUsername(String username) {
        return repository.existsByUsername(username);
    }

    public User save(User user) {
        return repository.save(user);
    }
}
