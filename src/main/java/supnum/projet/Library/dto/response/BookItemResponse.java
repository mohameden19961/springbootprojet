package supnum.projet.Library.dto.response;

import supnum.projet.Library.data.entities.enums.BookItemStatus;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BookItemResponse {
    private Long id;
    private String barcode;
    private BookItemStatus status;
    private Long version;
    private Long bookId;
}
