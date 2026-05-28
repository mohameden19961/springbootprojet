package supnum.projet.Library.dto.response;

import supnum.projet.Library.data.entities.enums.BorrowStatus;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BorrowResponse {
    private Long id;
    private Long memberId;
    private String memberEmail;
    private Long bookItemId;
    private String bookItemBarcode;
    private Integer renewalCount;
    private BorrowStatus status;
}
