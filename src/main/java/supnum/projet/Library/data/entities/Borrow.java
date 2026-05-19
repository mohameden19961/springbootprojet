package supnum.projet.Library.data.entities;

import jakarta.persistence.*;
import lombok.*;
import supnum.projet.Library.data.enums.BorrowStatus;
import java.time.LocalDateTime;

@Entity
@Table(name = "borrow")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Borrow {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_item_id", nullable = false)
    private BookItem bookItem;
    
    @Column(nullable = false)
    private Integer renewalCount = 0;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BorrowStatus status = BorrowStatus.ACTIVE;
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime borrowedAt = LocalDateTime.now();
    
    @Column
    private LocalDateTime returnedAt;
    
    @Column
    private LocalDateTime dueDate;
    
    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
    private Boolean deleted = false;
}
