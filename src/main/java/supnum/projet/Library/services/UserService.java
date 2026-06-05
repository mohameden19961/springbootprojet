package supnum.projet.Library.services;

import supnum.projet.Library.data.entities.User;
import supnum.projet.Library.data.repositories.UserRepository;
import supnum.projet.Library.dto.UpdateCredentialsDTO;
import supnum.projet.Library.dto.UserRegistrationDTO;
import supnum.projet.Library.dto.response.UserResponse;
import supnum.projet.Library.exceptions.DuplicateResourceException;
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

    public List<UserResponse> findAll() {
        return repository.findAll().stream().map(this::toResponse).toList();
    }

    public Optional<User> findByUsername(String username) {
        return repository.findByUsername(username);
    }

    public UserResponse updateCredentials(UpdateCredentialsDTO dto) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = repository.findByUsername(currentUsername)
            .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé avec le nom : " + currentUsername));

        if (!passwordEncoder.matches(dto.getCurrentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Mot de passe actuel incorrect");
        }

        if (dto.getNewUsername() != null && !dto.getNewUsername().isBlank()) {
            if (!dto.getNewUsername().equals(currentUsername) && repository.existsByUsername(dto.getNewUsername())) {
                throw new DuplicateResourceException("Ce nom d'utilisateur est déjà pris");
            }
            user.setUsername(dto.getNewUsername());
        }

        if (dto.getNewPassword() != null && !dto.getNewPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        }

        return toResponse(repository.save(user));
    }

    public UserResponse register(UserRegistrationDTO dto) {
        if (repository.existsByUsername(dto.getUsername())) {
            throw new DuplicateResourceException("Un utilisateur avec ce nom d'utilisateur existe déjà");
        }

        User user = new User(
            dto.getUsername(),
            passwordEncoder.encode(dto.getPassword()),
            dto.getRole().toUpperCase()
        );

        return toResponse(repository.save(user));
    }

    private UserResponse toResponse(User user) {
        return new UserResponse(user.getId(), user.getUsername(), user.getRole());
    }
}
