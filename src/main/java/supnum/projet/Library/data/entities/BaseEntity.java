package supnum.projet.Library.data.entities;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@MappedSuperclass
public abstract class BaseEntity {
    
    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;
}
