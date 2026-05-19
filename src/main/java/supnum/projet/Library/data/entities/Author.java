package supnum.projet.Library.data.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "author")
@SQLRestriction("deleted = false")
public class Author extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nationality_code")
    private Nationality nationality;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Nationality getNationality() { return nationality; }
    public void setNationality(Nationality nationality) { this.nationality = nationality; }
}
