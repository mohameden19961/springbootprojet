package supnum.projet.Library.services;

import supnum.projet.Library.dao.UserDao;
import supnum.projet.Library.data.entities.User;
import supnum.projet.Library.dto.UserRegistrationDTO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserDao userDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(UserRegistrationDTO dto) {
        if (userDao.existsByUsername(dto.getUsername())) {
            throw new RuntimeException("Un utilisateur avec ce nom d'utilisateur existe déjà");
        }

        User user = new User(
            dto.getUsername(),
            passwordEncoder.encode(dto.getPassword()),
            dto.getRole().toUpperCase()
        );

        return userDao.save(user);
    }
}
