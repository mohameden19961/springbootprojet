package supnum.projet.Library.dto;

import supnum.projet.Library.data.entities.Publisher;

public class PublisherResponseDTO {
    private Long id;
    private String name;
    private String email;

    public PublisherResponseDTO() {}

    public PublisherResponseDTO(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public static PublisherResponseDTO from(Publisher entity) {
        if (entity == null) return null;
        return new PublisherResponseDTO(entity.getId(), entity.getName(), entity.getEmail());
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
