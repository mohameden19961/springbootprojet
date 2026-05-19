package supnum.projet.Library.dto;

import supnum.projet.Library.data.entities.BookItem;
import supnum.projet.Library.data.entities.Borrow;
import supnum.projet.Library.data.entities.Member;
import supnum.projet.Library.data.entities.enums.BorrowStatus;

public class BorrowResponseDTO {
    private Long id;
    private Long memberId;
    private Long bookItemId;
    private String bookItemBarcode;
    private Integer renewalCount;
    private BorrowStatus status;

    public BorrowResponseDTO() {}

    public BorrowResponseDTO(Long id, Long memberId, Long bookItemId,
                             String bookItemBarcode, Integer renewalCount, BorrowStatus status) {
        this.id = id;
        this.memberId = memberId;
        this.bookItemId = bookItemId;
        this.bookItemBarcode = bookItemBarcode;
        this.renewalCount = renewalCount;
        this.status = status;
    }

    public static BorrowResponseDTO from(Borrow entity) {
        if (entity == null) return null;
        Member m = entity.getMember();
        BookItem bi = entity.getBookItem();
        return new BorrowResponseDTO(
            entity.getId(),
            m != null ? m.getId() : null,
            bi != null ? bi.getId() : null,
            bi != null ? bi.getBarcode() : null,
            entity.getRenewalCount(),
            entity.getStatus()
        );
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getMemberId() { return memberId; }
    public void setMemberId(Long id) { this.memberId = id; }
    public Long getBookItemId() { return bookItemId; }
    public void setBookItemId(Long id) { this.bookItemId = id; }
    public String getBookItemBarcode() { return bookItemBarcode; }
    public void setBookItemBarcode(String b) { this.bookItemBarcode = b; }
    public Integer getRenewalCount() { return renewalCount; }
    public void setRenewalCount(Integer c) { this.renewalCount = c; }
    public BorrowStatus getStatus() { return status; }
    public void setStatus(BorrowStatus s) { this.status = s; }
}
