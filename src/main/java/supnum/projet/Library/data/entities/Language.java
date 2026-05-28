package supnum.projet.Library.data.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Table(name = "language")
public class Language {
    @Id
    @Column(length = 10, nullable = false)
    private String code;

    @Column(length = 50, nullable = false)
    private String name;
}
