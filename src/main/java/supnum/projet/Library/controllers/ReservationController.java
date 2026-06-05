package supnum.projet.Library.controllers;

import supnum.projet.Library.dto.ReservationDTO;
import supnum.projet.Library.dto.response.ReservationResponse;
import supnum.projet.Library.services.ReservationService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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

    @GetMapping
    public Page<ReservationResponse> getAll(Pageable pageable) {
        return service.findAll(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> reserve(@Valid @RequestBody ReservationDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.reserve(dto));
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<ReservationResponse> cancel(@PathVariable Long id) {
        return ResponseEntity.ok(service.cancel(id));
    }

    @GetMapping("/queue/{bookId}")
    public ResponseEntity<List<ReservationResponse>> getQueue(@PathVariable Long bookId) {
        return ResponseEntity.ok(service.getQueueForBook(bookId));
    }
}
