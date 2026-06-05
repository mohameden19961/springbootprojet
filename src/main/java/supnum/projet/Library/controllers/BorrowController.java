package supnum.projet.Library.controllers;

import supnum.projet.Library.dto.response.BorrowResponse;
import supnum.projet.Library.services.BorrowService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/borrows")
public class BorrowController {

    private final BorrowService service;

    public BorrowController(BorrowService service) {
        this.service = service;
    }

    @GetMapping
    public Page<BorrowResponse> getAll(Pageable pageable) {
        return service.findAll(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BorrowResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping("/checkout")
    public ResponseEntity<BorrowResponse> checkout(@RequestParam Long memberId, @RequestParam String barcode) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.borrowBook(memberId, barcode));
    }

    @PostMapping("/{borrowId}/return")
    public ResponseEntity<BorrowResponse> returnBook(@PathVariable Long borrowId) {
        return ResponseEntity.ok(service.returnBook(borrowId));
    }

    @PostMapping("/{borrowId}/renew")
    public ResponseEntity<BorrowResponse> renew(@PathVariable Long borrowId) {
        return ResponseEntity.ok(service.renewBorrow(borrowId));
    }
}
