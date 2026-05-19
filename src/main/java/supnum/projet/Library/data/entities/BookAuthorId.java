package supnum.projet.Library.data.entities;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookAuthorId implements Serializable {
    
    @Column(name = "book_id")
    private Long bookId;
    
    @Column(name = "author_id")
    private Long authorId;
}
