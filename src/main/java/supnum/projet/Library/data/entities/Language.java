package supnum.projet.Library.data.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "language")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Language {
    
    @Id
    private String code;
    
    @Column(nullable = false, unique = true)
    private String name;
}
