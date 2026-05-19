package supnum.projet.Library.data.entities;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Column;

@MappedSuperclass
public abstract class BaseEntity {
    
    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;

    public boolean isDeleted() { 
        return deleted; 
    }
    
    public void setDeleted(boolean deleted) { 
        this.deleted = deleted; 
    }
}
