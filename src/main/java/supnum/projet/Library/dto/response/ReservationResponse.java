package supnum.projet.Library.dto.response;

import supnum.projet.Library.data.entities.enums.ReservationStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class ReservationResponse {
    private Long id;
    private Long memberId;
    private String memberEmail;
    private Long bookId;
    private String bookTitle;
    private Integer queuePosition;
    private LocalDateTime reservationDate;
    private LocalDateTime expirationDate;
    private ReservationStatus status;
}
