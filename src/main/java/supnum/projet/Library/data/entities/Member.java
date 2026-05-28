package supnum.projet.Library.data.entities;

import supnum.projet.Library.data.entities.enums.MemberType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

@Getter @Setter
@Entity
@Table(name = "member")
@SQLRestriction("deleted = false")
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "member_type", nullable = false)
    private MemberType memberType;

    @Column(name = "max_borrows", nullable = false)
    private Integer maxBorrows;
}
