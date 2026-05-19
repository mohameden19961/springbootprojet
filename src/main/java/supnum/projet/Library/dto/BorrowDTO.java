package supnum.projet.Library.dto;

import lombok.*;
import supnum.projet.Library.data.enums.BorrowStatus;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BorrowDTO {
    private Long id;
    private Long memberId;
    private MemberDTO member;
    private Long bookItemId;
    private BookItemDTO bookItem;
    private Integer renewalCount;
    private BorrowStatus status;
    private LocalDateTime borrowedAt;
    private LocalDateTime returnedAt;
    private LocalDateTime dueDate;
}
