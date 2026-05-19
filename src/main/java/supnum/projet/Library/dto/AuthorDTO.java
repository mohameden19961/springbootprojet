package supnum.projet.Library.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AuthorDTO {
    private Long id;

    @NotBlank(message = "Le nom de l'auteur est requis")
    private String name;

    @NotBlank(message = "Le code de nationalité est requis")
    @Size(max = 10)
    private String nationalityCode;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getNationalityCode() { return nationalityCode; }
    public void setNationalityCode(String nationalityCode) { this.nationalityCode = nationalityCode; }
}
