package supnum.projet.Library.dto;

import lombok.*;
import supnum.projet.Library.data.enums.AuthorRole;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookAuthorDTO {
    private Long bookId;
    private Long authorId;
    private AuthorRole role;
}
