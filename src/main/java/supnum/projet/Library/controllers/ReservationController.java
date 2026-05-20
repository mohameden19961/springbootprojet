package supnum.projet.Library.controllers;

import supnum.projet.Library.data.entities.Reservation;
import supnum.projet.Library.dto.ReservationDTO;
import supnum.projet.Library.services.ReservationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService service;

    public ReservationController(ReservationService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Reservation> reserve(@Valid @RequestBody ReservationDTO dto) {
        return ResponseEntity.ok(service.reserve(dto));
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<Reservation> cancel(@PathVariable Long id) {
        return ResponseEntity.ok(service.cancel(id));
    }

    @GetMapping("/queue/{bookId}")
    public ResponseEntity<List<Reservation>> getQueue(@PathVariable Long bookId) {
        return ResponseEntity.ok(service.getQueueForBook(bookId));
    }
}
