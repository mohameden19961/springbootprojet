package supnum.projet.Library.dto;

import supnum.projet.Library.data.entities.Book;
import supnum.projet.Library.data.entities.Member;
import supnum.projet.Library.data.entities.Reservation;
import supnum.projet.Library.data.entities.enums.ReservationStatus;

public class ReservationResponseDTO {
    private Long id;
    private Long memberId;
    private Long bookId;
    private String bookTitle;
    private Integer queuePosition;
    private ReservationStatus status;

    public ReservationResponseDTO() {}

    public ReservationResponseDTO(Long id, Long memberId, Long bookId,
                                  String bookTitle, Integer queuePosition, ReservationStatus status) {
        this.id = id;
        this.memberId = memberId;
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.queuePosition = queuePosition;
        this.status = status;
    }

    public static ReservationResponseDTO from(Reservation entity) {
        if (entity == null) return null;
        Member m = entity.getMember();
        Book b = entity.getBook();
        return new ReservationResponseDTO(
            entity.getId(),
            m != null ? m.getId() : null,
            b != null ? b.getId() : null,
            b != null ? b.getTitle() : null,
            entity.getQueuePosition(),
            entity.getStatus()
        );
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getMemberId() { return memberId; }
    public void setMemberId(Long id) { this.memberId = id; }
    public Long getBookId() { return bookId; }
    public void setBookId(Long id) { this.bookId = id; }
    public String getBookTitle() { return bookTitle; }
    public void setBookTitle(String t) { this.bookTitle = t; }
    public Integer getQueuePosition() { return queuePosition; }
    public void setQueuePosition(Integer p) { this.queuePosition = p; }
    public ReservationStatus getStatus() { return status; }
    public void setStatus(ReservationStatus s) { this.status = s; }
}
