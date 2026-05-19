package supnum.projet.Library.dto;

import lombok.*;
import supnum.projet.Library.data.enums.ReservationStatus;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationDTO {
    private Long id;
    private Long memberId;
    private MemberDTO member;
    private Long bookId;
    private BookDTO book;
    private Integer queuePosition;
    private ReservationStatus status;
    private LocalDateTime createdAt;
}
