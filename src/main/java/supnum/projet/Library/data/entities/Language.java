package supnum.projet.Library.data.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "language")
public class Language {
    @Id
    @Column(length = 10, nullable = false)
    private String code;

    @Column(length = 50, nullable = false)
    private String name;

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
