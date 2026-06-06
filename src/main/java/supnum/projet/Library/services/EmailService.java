package supnum.projet.Library.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;
    private final String from;

    public EmailService(JavaMailSender mailSender,
                        @Value("${spring.mail.from:noreply@library.supnum.tn}") String from) {
        this.mailSender = mailSender;
        this.from = from;
    }

    public void sendBorrowConfirmation(String to, String bookTitle, String dueDate) {
        send(to, "Confirmation d'emprunt",
            "Bonjour,\n\n"
            + "Vous avez emprunté le livre : " + bookTitle + "\n"
            + "Date de retour prévue : " + dueDate + "\n\n"
            + "Merci de votre visite à la bibliothèque.");
    }

    public void sendReturnConfirmation(String to, String bookTitle) {
        send(to, "Confirmation de retour",
            "Bonjour,\n\n"
            + "Le livre \"" + bookTitle + "\" a bien été retourné.\n\n"
            + "Merci de votre visite à la bibliothèque.");
    }

    public void sendReservationAvailable(String to, String bookTitle) {
        send(to, "Livre disponible - Réservation",
            "Bonjour,\n\n"
            + "Le livre \"" + bookTitle + "\" que vous avez réservé est maintenant disponible.\n"
            + "Veuillez passer à la bibliothèque pour l'emprunter sous 48h.\n\n"
            + "Cordialement.");
    }

    public void sendOverdueNotice(String to, String bookTitle, String dueDate) {
        send(to, "Rappel - Retard de retour",
            "Bonjour,\n\n"
            + "Le livre \"" + bookTitle + "\" devait être retourné le " + dueDate + ".\n"
            + "Nous vous invitons à le retourner dès que possible.\n\n"
            + "Cordialement.");
    }

    private void send(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            mailSender.send(message);
            log.info("Email envoyé à {} : {}", to, subject);
        } catch (Exception e) {
            log.warn("Impossible d'envoyer l'email à {} : {}", to, e.getMessage());
        }
    }
}
