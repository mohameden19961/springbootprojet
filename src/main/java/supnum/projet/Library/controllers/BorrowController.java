package supnum.projet.Library.controllers;

import supnum.projet.Library.dto.BorrowResponseDTO;
import supnum.projet.Library.services.BorrowService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/borrows")
@Validated
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
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<BorrowResponseDTO> checkout(
            @RequestParam @NotNull @Positive Long memberId,
            @RequestParam @NotBlank String barcode) {
        return ResponseEntity.ok(BorrowResponseDTO.from(service.borrowBook(memberId, barcode)));
    }

    @PostMapping("/{borrowId}/return")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<BorrowResponseDTO> returnBook(@PathVariable @NotNull @Positive Long borrowId) {
        return ResponseEntity.ok(BorrowResponseDTO.from(service.returnBook(borrowId)));
    }

    @PostMapping("/{borrowId}/renew")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<BorrowResponseDTO> renew(@PathVariable @NotNull @Positive Long borrowId) {
        return ResponseEntity.ok(BorrowResponseDTO.from(service.renewBorrow(borrowId)));
    }
}
