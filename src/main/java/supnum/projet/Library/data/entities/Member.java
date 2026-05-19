package supnum.projet.Library.data.entities;

import jakarta.persistence.*;
import lombok.*;
import supnum.projet.Library.data.enums.MemberType;
import java.util.*;

@Entity
@Table(name = "member")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberType memberType;
    
    @Column(nullable = false)
    private Integer maxBorrows;
    
    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Borrow> borrows = new HashSet<>();
    
    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Reservation> reservations = new HashSet<>();
    
    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
    private Boolean deleted = false;
}
