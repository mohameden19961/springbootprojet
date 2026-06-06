package supnum.projet.Library.websocket;

import supnum.projet.Library.data.entities.Book;
import supnum.projet.Library.data.entities.Borrow;
import supnum.projet.Library.data.entities.Member;
import supnum.projet.Library.data.entities.Reservation;
import supnum.projet.Library.dto.response.BorrowResponse;
import supnum.projet.Library.dto.response.ReservationResponse;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    public NotificationService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void notifyBorrowCreated(Borrow borrow, BorrowResponse response) {
        Member member = borrow.getMember();
        if (member != null) {
            WebSocketEvent event = new WebSocketEvent(
                "BORROW_CREATED",
                "Emprunt confirmé : " + borrow.getBookItem().getBarcode(),
                response
            );
            messagingTemplate.convertAndSend("/topic/notifications/" + member.getId(), event);
        }

        WebSocketEvent adminEvent = new WebSocketEvent(
            "BORROW_CREATED",
            "Nouvel emprunt - Membre: " + member.getEmail() + ", Livre: " + borrow.getBookItem().getBarcode(),
            response
        );
        messagingTemplate.convertAndSend("/topic/admin", adminEvent);
    }

    public void notifyBorrowReturned(Borrow borrow, BorrowResponse response) {
        Member member = borrow.getMember();
        if (member != null) {
            WebSocketEvent event = new WebSocketEvent(
                "BORROW_RETURNED",
                "Retour confirmé pour l'exemplaire : " + borrow.getBookItem().getBarcode(),
                response
            );
            messagingTemplate.convertAndSend("/topic/notifications/" + member.getId(), event);
        }

        WebSocketEvent adminEvent = new WebSocketEvent(
            "BORROW_RETURNED",
            "Retour d'emprunt - Membre: " + member.getEmail() + ", Exemplaire: " + borrow.getBookItem().getBarcode(),
            response
        );
        messagingTemplate.convertAndSend("/topic/admin", adminEvent);
    }

    public void notifyBookAvailable(Long memberId, Book book) {
        WebSocketEvent event = new WebSocketEvent(
            "BOOK_AVAILABLE",
            "Le livre \"" + book.getTitle() + "\" est maintenant disponible !",
            book.getId()
        );
        messagingTemplate.convertAndSend("/topic/notifications/" + memberId, event);
    }

    public void notifyReservationCreated(Reservation reservation, ReservationResponse response) {
        WebSocketEvent adminEvent = new WebSocketEvent(
            "RESERVATION_CREATED",
            "Nouvelle réservation - Membre: " + reservation.getMember().getEmail()
                + ", Livre: " + reservation.getBook().getTitle(),
            response
        );
        messagingTemplate.convertAndSend("/topic/admin", adminEvent);
    }

    public void notifyReservationCancelled(Reservation reservation, ReservationResponse response) {
        Member member = reservation.getMember();
        if (member != null) {
            WebSocketEvent event = new WebSocketEvent(
                "RESERVATION_CANCELLED",
                "Votre réservation pour \"" + reservation.getBook().getTitle() + "\" a été annulée.",
                response
            );
            messagingTemplate.convertAndSend("/topic/notifications/" + member.getId(), event);
        }

        WebSocketEvent adminEvent = new WebSocketEvent(
            "RESERVATION_CANCELLED",
            "Réservation annulée - Membre: " + reservation.getMember().getEmail()
                + ", Livre: " + reservation.getBook().getTitle(),
            response
        );
        messagingTemplate.convertAndSend("/topic/admin", adminEvent);
    }
}
