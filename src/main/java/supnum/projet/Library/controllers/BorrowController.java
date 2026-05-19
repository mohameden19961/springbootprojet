package supnum.projet.Library.controllers;

import supnum.projet.Library.dto.BorrowResponseDTO;
import supnum.projet.Library.services.BorrowService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/borrows")
public class BorrowController {

    private final BorrowService service;

    public BorrowController(BorrowService service) {
        this.service = service;
    }

    @PostMapping("/checkout")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<BorrowResponseDTO> checkout(@RequestParam Long memberId, @RequestParam String barcode) {
        return ResponseEntity.ok(BorrowResponseDTO.from(service.borrowBook(memberId, barcode)));
    }

    @PostMapping("/{borrowId}/return")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<BorrowResponseDTO> returnBook(@PathVariable Long borrowId) {
        return ResponseEntity.ok(BorrowResponseDTO.from(service.returnBook(borrowId)));
    }

    @PostMapping("/{borrowId}/renew")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<BorrowResponseDTO> renew(@PathVariable Long borrowId) {
        return ResponseEntity.ok(BorrowResponseDTO.from(service.renewBorrow(borrowId)));
    }
}
