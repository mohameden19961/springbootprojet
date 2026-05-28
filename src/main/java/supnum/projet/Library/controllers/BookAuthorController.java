package supnum.projet.Library.controllers;

import supnum.projet.Library.data.entities.BookAuthor;
import supnum.projet.Library.data.entities.enums.AuthorRole;
import supnum.projet.Library.services.BookAuthorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books/{bookId}/authors")
public class BookAuthorController {

    private final BookAuthorService service;

    public BookAuthorController(BookAuthorService service) {
        this.service = service;
    }

    @GetMapping
    public List<BookAuthor> getAuthors(@PathVariable Long bookId) {
        return service.findAllByBook(bookId);
    }

    @PostMapping("/{authorId}")
    public ResponseEntity<BookAuthor> addAuthor(
            @PathVariable Long bookId,
            @PathVariable Long authorId,
            @RequestParam(defaultValue = "MAIN_AUTHOR") AuthorRole role) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.addAuthorToBook(bookId, authorId, role));
    }

    @DeleteMapping("/{authorId}")
    public ResponseEntity<Void> removeAuthor(@PathVariable Long bookId, @PathVariable Long authorId) {
        service.removeAuthorFromBook(bookId, authorId);
        return ResponseEntity.noContent().build();
    }
}
