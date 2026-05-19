package supnum.projet.Library.data.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "language")
public class Language {

    @Id
    @Column(name = "code", length = 10)
    private String code;

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
}
