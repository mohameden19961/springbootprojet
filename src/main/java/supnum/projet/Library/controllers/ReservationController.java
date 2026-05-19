package supnum.projet.Library.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import supnum.projet.Library.dto.ReservationDTO;
import supnum.projet.Library.data.enums.ReservationStatus;
import supnum.projet.Library.services.ReservationService;

@RestController
@RequestMapping("/api/v1/reservations")
@RequiredArgsConstructor
@Validated
public class ReservationController {
    
    private final ReservationService reservationService;
    
    @PostMapping("/member/{memberId}/book/{bookId}")
    public ResponseEntity<ReservationDTO> createReservation(
            @PathVariable Long memberId,
            @PathVariable Long bookId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(reservationService.createReservation(memberId, bookId));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ReservationDTO> getReservationById(@PathVariable Long id) {
        return ResponseEntity.ok(reservationService.getReservationById(id));
    }
    
    @GetMapping("/member/{memberId}")
    public ResponseEntity<Page<ReservationDTO>> getReservationsByMember(
            @PathVariable Long memberId,
            Pageable pageable) {
        return ResponseEntity.ok(reservationService.getReservationsByMember(memberId, pageable));
    }
    
    @GetMapping("/book/{bookId}/queue")
    public ResponseEntity<Page<ReservationDTO>> getReservationQueue(
            @PathVariable Long bookId,
            Pageable pageable) {
        return ResponseEntity.ok(reservationService.getReservationQueueForBook(bookId, pageable));
    }
    
    @GetMapping("/status/{status}")
    public ResponseEntity<Page<ReservationDTO>> getReservationsByStatus(
            @PathVariable ReservationStatus status,
            Pageable pageable) {
        return ResponseEntity.ok(reservationService.getReservationsByStatus(status, pageable));
    }
    
    @PutMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelReservation(@PathVariable Long id) {
        reservationService.cancelReservation(id);
        return ResponseEntity.noContent().build();
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        reservationService.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }
}
