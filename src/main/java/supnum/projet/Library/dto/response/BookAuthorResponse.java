package supnum.projet.Library.dto.response;

import supnum.projet.Library.data.entities.enums.AuthorRole;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BookAuthorResponse {
    private Long bookId;
    private String bookTitle;
    private Long authorId;
    private String authorName;
    private AuthorRole role;
}
