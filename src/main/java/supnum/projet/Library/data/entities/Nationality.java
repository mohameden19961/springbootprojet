package supnum.projet.Library.data.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "nationality")
public class Nationality {
    @Id
    @Column(length = 10, nullable = false)
    private String code;

    @Column(length = 50, nullable = false)
    private String name;
}
