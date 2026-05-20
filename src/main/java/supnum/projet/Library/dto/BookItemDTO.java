package supnum.projet.Library.dto;

import supnum.projet.Library.data.entities.enums.BookItemStatus;
import jakarta.validation.constraints.NotBlank;

public class BookItemDTO {
    private Long id;

    @NotBlank(message = "Le code-barres est requis")
    private String barcode;

    private BookItemStatus status;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getBarcode() { return barcode; }
    public void setBarcode(String barcode) { this.barcode = barcode; }
    public BookItemStatus getStatus() { return status; }
    public void setStatus(BookItemStatus status) { this.status = status; }
}
