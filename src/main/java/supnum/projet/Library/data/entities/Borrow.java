package supnum.projet.Library.data.entities;

import supnum.projet.Library.data.entities.enums.BorrowStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

@Entity
@Table(name = "borrow")
public class Borrow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_item_id", nullable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    private BookItem bookItem;

    @Column(name = "renewal_count", nullable = false)
    private Integer renewalCount = 0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BorrowStatus status = BorrowStatus.ACTIVE;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Member getMember() { return member; }
    public void setMember(Member member) { this.member = member; }
    public BookItem getBookItem() { return bookItem; }
    public void setBookItem(BookItem bookItem) { this.bookItem = bookItem; }
    public Integer getRenewalCount() { return renewalCount; }
    public void setRenewalCount(Integer renewalCount) { this.renewalCount = renewalCount; }
    public BorrowStatus getStatus() { return status; }
    public void setStatus(BorrowStatus status) { this.status = status; }
}
