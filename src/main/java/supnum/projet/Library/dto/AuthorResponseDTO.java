package supnum.projet.Library.dto;

import supnum.projet.Library.data.entities.Author;
import supnum.projet.Library.data.entities.Nationality;

public class AuthorResponseDTO {
    private Long id;
    private String name;
    private String nationalityCode;
    private String nationalityName;

    public AuthorResponseDTO() {}

    public AuthorResponseDTO(Long id, String name, String nationalityCode, String nationalityName) {
        this.id = id;
        this.name = name;
        this.nationalityCode = nationalityCode;
        this.nationalityName = nationalityName;
    }

    public static AuthorResponseDTO from(Author entity) {
        if (entity == null) return null;
        Nationality nat = entity.getNationality();
        return new AuthorResponseDTO(
            entity.getId(),
            entity.getName(),
            nat != null ? nat.getCode() : null,
            nat != null ? nat.getName() : null
        );
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getNationalityCode() { return nationalityCode; }
    public void setNationalityCode(String code) { this.nationalityCode = code; }
    public String getNationalityName() { return nationalityName; }
    public void setNationalityName(String name) { this.nationalityName = name; }
}
