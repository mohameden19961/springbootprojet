package supnum.projet.Library.controllers;

import supnum.projet.Library.dto.ReservationDTO;
import supnum.projet.Library.dto.ReservationResponseDTO;
import supnum.projet.Library.services.ReservationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@Validated
public class ReservationController {

    private final ReservationService service;

    public ReservationController(ReservationService service) {
        this.service = service;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<ReservationResponseDTO> reserve(@Valid @RequestBody ReservationDTO dto) {
        return ResponseEntity.ok(ReservationResponseDTO.from(service.reserve(dto)));
    }

    @PostMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<ReservationResponseDTO> cancel(@PathVariable @NotNull @Positive Long id) {
        return ResponseEntity.ok(ReservationResponseDTO.from(service.cancel(id)));
    }

    @GetMapping("/queue/{bookId}")
    public ResponseEntity<List<ReservationResponseDTO>> getQueue(@PathVariable @NotNull @Positive Long bookId) {
        List<ReservationResponseDTO> queue = service.getQueueForBook(bookId).stream()
            .map(ReservationResponseDTO::from)
            .toList();
        return ResponseEntity.ok(queue);
    }
}
