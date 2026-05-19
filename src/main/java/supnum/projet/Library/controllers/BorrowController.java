package supnum.projet.Library.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import supnum.projet.Library.dto.BorrowDTO;
import supnum.projet.Library.data.enums.BorrowStatus;
import supnum.projet.Library.services.BorrowService;

@RestController
@RequestMapping("/api/v1/borrows")
@RequiredArgsConstructor
@Validated
public class BorrowController {
    
    private final BorrowService borrowService;
    
    @PostMapping("/member/{memberId}/item/{bookItemId}")
    public ResponseEntity<BorrowDTO> borrowBook(
            @PathVariable Long memberId,
            @PathVariable Long bookItemId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(borrowService.borrowBook(memberId, bookItemId));
    }
    
    @PutMapping("/{borrowId}/return")
    public ResponseEntity<BorrowDTO> returnBook(@PathVariable Long borrowId) {
        return ResponseEntity.ok(borrowService.returnBook(borrowId));
    }
    
    @PutMapping("/{borrowId}/renew")
    public ResponseEntity<BorrowDTO> renewBorrow(@PathVariable Long borrowId) {
        return ResponseEntity.ok(borrowService.renewBorrow(borrowId));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<BorrowDTO> getBorrowById(@PathVariable Long id) {
        return ResponseEntity.ok(borrowService.getBorrowById(id));
    }
    
    @GetMapping("/member/{memberId}")
    public ResponseEntity<Page<BorrowDTO>> getBorrowsByMember(
            @PathVariable Long memberId,
            Pageable pageable) {
        return ResponseEntity.ok(borrowService.getBorrowsByMember(memberId, pageable));
    }
    
    @GetMapping("/status/{status}")
    public ResponseEntity<Page<BorrowDTO>> getBorrowsByStatus(
            @PathVariable BorrowStatus status,
            Pageable pageable) {
        return ResponseEntity.ok(borrowService.getBorrowsByStatus(status, pageable));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBorrow(@PathVariable Long id) {
        borrowService.deleteBorrow(id);
        return ResponseEntity.noContent().build();
    }
}
