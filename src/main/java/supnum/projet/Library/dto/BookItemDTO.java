package supnum.projet.Library.dto;

import lombok.*;
import jakarta.validation.constraints.*;
import supnum.projet.Library.data.enums.BookItemStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookItemDTO {
    private Long id;
    
    @NotBlank(message = "Barcode is required")
    private String barcode;
    
    private BookItemStatus status;
    
    @NotNull(message = "Book is required")
    private Long bookId;
    private BookDTO book;
    
    private Long version;
}
