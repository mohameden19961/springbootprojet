package supnum.projet.Library.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BookResponse {
    private Long id;
    private String title;
    private String isbn;
    private String languageCode;
    private String languageName;
    private Long categoryId;
    private String categoryName;
    private Long publisherId;
    private String publisherName;
}
