package supnum.projet.Library.controllers;

import supnum.projet.Library.services.GoogleBooksService;
import supnum.projet.Library.services.GoogleBooksService.GoogleBook;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/external/books")
public class GoogleBooksController {

    private final GoogleBooksService googleBooksService;

    public GoogleBooksController(GoogleBooksService googleBooksService) {
        this.googleBooksService = googleBooksService;
    }

    @GetMapping("/search")
    public ResponseEntity<List<GoogleBook>> search(@RequestParam String q,
                                                    @RequestParam(defaultValue = "10") int max) {
        return ResponseEntity.ok(googleBooksService.searchBooks(q, max));
    }

    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<GoogleBook> findByIsbn(@PathVariable String isbn) {
        GoogleBook book = googleBooksService.findByIsbn(isbn);
        if (book == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(book);
    }
}
