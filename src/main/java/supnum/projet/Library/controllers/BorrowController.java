package supnum.projet.Library.controllers;

import supnum.projet.Library.data.entities.Borrow;
import supnum.projet.Library.services.BorrowService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/borrows")
public class BorrowController {

    private final BorrowService service;

    public BorrowController(BorrowService service) {
        this.service = service;
    }

    @GetMapping
    public List<Borrow> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Borrow> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping("/checkout")
    public ResponseEntity<Borrow> checkout(@RequestParam Long memberId, @RequestParam String barcode) {
        return ResponseEntity.ok(service.borrowBook(memberId, barcode));
    }

    @PostMapping("/{borrowId}/return")
    public ResponseEntity<Borrow> returnBook(@PathVariable Long borrowId) {
        return ResponseEntity.ok(service.returnBook(borrowId));
    }

    @PostMapping("/{borrowId}/renew")
    public ResponseEntity<Borrow> renew(@PathVariable Long borrowId) {
        return ResponseEntity.ok(service.renewBorrow(borrowId));
    }
}
