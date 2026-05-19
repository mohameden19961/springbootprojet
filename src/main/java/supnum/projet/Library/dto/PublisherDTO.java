package supnum.projet.Library.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class PublisherDTO {
    private Long id;

    @NotBlank(message = "Le nom de l'éditeur est requis")
    @Size(max = 100)
    private String name;

    @Email(message = "L'email doit être valide")
    @Size(max = 100)
    private String email;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
