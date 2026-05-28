package supnum.projet.Library.dto.response;

import supnum.projet.Library.data.entities.enums.BorrowStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter @Setter
public class BorrowResponse {
    private Long id;
    private Long memberId;
    private String memberEmail;
    private Long bookItemId;
    private String bookItemBarcode;
    private Integer renewalCount;
    private LocalDateTime borrowDate;
    private LocalDate dueDate;
    private LocalDateTime returnDate;
    private BorrowStatus status;
}
