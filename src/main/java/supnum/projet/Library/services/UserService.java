package supnum.projet.Library.services;

import supnum.projet.Library.data.entities.User;
import supnum.projet.Library.data.repositories.UserRepository;
import supnum.projet.Library.dto.UpdateCredentialsDTO;
import supnum.projet.Library.dto.UserRegistrationDTO;
import supnum.projet.Library.exceptions.ResourceNotFoundException;
import java.util.List;
import java.util.Optional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> findAll() {
        return repository.findAll();
    }

    public Optional<User> findByUsername(String username) {
        return repository.findByUsername(username);
    }

    public User updateCredentials(UpdateCredentialsDTO dto) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = repository.findByUsername(currentUsername)
            .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        if (!passwordEncoder.matches(dto.getCurrentPassword(), user.getPassword())) {
            throw new RuntimeException("Mot de passe actuel incorrect");
        }

        if (dto.getNewUsername() != null && !dto.getNewUsername().isBlank()) {
            if (!dto.getNewUsername().equals(currentUsername) && repository.existsByUsername(dto.getNewUsername())) {
                throw new RuntimeException("Ce nom d'utilisateur est déjà pris");
            }
            user.setUsername(dto.getNewUsername());
        }

        if (dto.getNewPassword() != null && !dto.getNewPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        }

        return repository.save(user);
    }

    public User register(UserRegistrationDTO dto) {
        if (repository.existsByUsername(dto.getUsername())) {
            throw new RuntimeException("Un utilisateur avec ce nom d'utilisateur existe déjà");
        }

        User user = new User(
            dto.getUsername(),
            passwordEncoder.encode(dto.getPassword()),
            dto.getRole().toUpperCase()
        );

        return repository.save(user);
    }
}
