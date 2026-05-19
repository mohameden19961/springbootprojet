package supnum.projet.Library.dto;

import jakarta.validation.constraints.NotNull;
public class ReservationDTO {
      private Long id;
    @NotNull(message = "L'identifiant du membre est requis")
    private Long memberId;
    @NotNull(message = "L'identifiant du livre est requis")
    private Long bookId;
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getMemberId() { return memberId; }
    public void setMemberId(Long memberId) { this.memberId = memberId; }
    public Long getBookId() { return bookId; }
    public void setBookId(Long bookId) { this.bookId = bookId; }
}
