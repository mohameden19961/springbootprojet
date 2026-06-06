package supnum.projet.Library.scheduling;

import supnum.projet.Library.data.entities.Borrow;
import supnum.projet.Library.data.entities.Reservation;
import supnum.projet.Library.data.entities.enums.BorrowStatus;
import supnum.projet.Library.data.entities.enums.ReservationStatus;
import supnum.projet.Library.data.repositories.BorrowRepository;
import supnum.projet.Library.data.repositories.ReservationRepository;
import supnum.projet.Library.services.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class ScheduledTasks {

    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

    private final BorrowRepository borrowRepository;
    private final ReservationRepository reservationRepository;
    private final EmailService emailService;

    public ScheduledTasks(BorrowRepository borrowRepository,
                          ReservationRepository reservationRepository,
                          EmailService emailService) {
        this.borrowRepository = borrowRepository;
        this.reservationRepository = reservationRepository;
        this.emailService = emailService;
    }

    @Scheduled(cron = "0 0 8 * * ?")
    @Transactional
    public void checkOverdueBorrows() {
        log.info("Vérification des emprunts en retard...");
        List<Borrow> overdue = borrowRepository.findAll().stream()
            .filter(b -> b.getStatus() == BorrowStatus.ACTIVE)
            .filter(b -> b.getDueDate() != null && b.getDueDate().isBefore(LocalDate.now()))
            .toList();

        for (Borrow borrow : overdue) {
            if (borrow.getMember() != null && borrow.getMember().getEmail() != null) {
                emailService.sendOverdueNotice(
                    borrow.getMember().getEmail(),
                    borrow.getBookItem().getBook().getTitle(),
                    borrow.getDueDate().toString()
                );
            }
        }
        log.info("{} emprunts en retard notifiés.", overdue.size());
    }

    @Scheduled(cron = "0 0 6 * * ?")
    @Transactional
    public void expireOldReservations() {
        log.info("Expiration des vieilles réservations...");
        LocalDateTime threshold = LocalDateTime.now().minusDays(7);
        List<Reservation> expired = reservationRepository.findAll().stream()
            .filter(r -> r.getStatus() == ReservationStatus.PENDING)
            .filter(r -> r.getReservationDate() != null && r.getReservationDate().isBefore(threshold))
            .toList();

        for (Reservation reservation : expired) {
            reservation.setStatus(ReservationStatus.CANCELLED);
        }
        reservationRepository.saveAll(expired);
        log.info("{} réservations expirées annulées.", expired.size());
    }
}
