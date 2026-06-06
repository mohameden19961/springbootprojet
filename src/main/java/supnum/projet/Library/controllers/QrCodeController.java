package supnum.projet.Library.controllers;

import supnum.projet.Library.data.entities.BookItem;
import supnum.projet.Library.data.repositories.BookItemRepository;
import supnum.projet.Library.exceptions.ResourceNotFoundException;
import supnum.projet.Library.services.QrCodeService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/qrcode")
public class QrCodeController {

    private final QrCodeService qrCodeService;
    private final BookItemRepository bookItemRepository;

    public QrCodeController(QrCodeService qrCodeService, BookItemRepository bookItemRepository) {
        this.qrCodeService = qrCodeService;
        this.bookItemRepository = bookItemRepository;
    }

    @GetMapping("/book-item/{id}")
    public ResponseEntity<byte[]> getQrForBookItem(@PathVariable Long id,
                                                    @RequestParam(defaultValue = "300") int size) {
        BookItem item = bookItemRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Exemplaire non trouvé avec l'id : " + id));

        String data = String.format("{\"id\":%d,\"barcode\":\"%s\"}", item.getId(), item.getBarcode());
        byte[] qr = qrCodeService.generateQrCode(data, size, size);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(qr);
    }

    @GetMapping("/text")
    public ResponseEntity<byte[]> getQrForText(@RequestParam String text,
                                                @RequestParam(defaultValue = "300") int size) {
        byte[] qr = qrCodeService.generateQrCode(text, size, size);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(qr);
    }
}
