package supnum.projet.Library.dto.response;

import supnum.projet.Library.data.entities.enums.ReservationStatus;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ReservationResponse {
    private Long id;
    private Long memberId;
    private String memberEmail;
    private Long bookId;
    private String bookTitle;
    private Integer queuePosition;
    private ReservationStatus status;
}
