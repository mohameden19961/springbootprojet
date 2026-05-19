package supnum.projet.Library.data.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "nationality")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Nationality {
    
    @Id
    private String code;
    
    @Column(nullable = false, unique = true)
    private String name;
}
