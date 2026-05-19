package supnum.projet.Library.dto;

import lombok.*;
import jakarta.validation.constraints.*;
import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookDTO {
    private Long id;
    
    @NotBlank(message = "Book title is required")
    private String title;
    
    @NotBlank(message = "ISBN is required")
    @Pattern(regexp = "^(?:ISBN(?:-1[03])?:? )?(?=[0-9X]{10}$|(?=(?:[0-9]+[- ]){3})[- 0-9X]{13}$|97[89][0-9]{10}$|(?=(?:[0-9]+[- ]){4})[- 0-9]{17}$)(?:97[89][- ]?)?[0-9]{1,5}[- ]?[0-9]+[- ]?[0-9]+[- ]?[0-9X]$", 
    message = "ISBN format is invalid")
    private String isbn;
    
    @NotNull(message = "Language code is required")
    private String languageCode;
    private LanguageDTO language;
    
    @NotNull(message = "Category is required")
    private Long categoryId;
    private CategoryDTO category;
    
    @NotNull(message = "Publisher is required")
    private Long publisherId;
    private PublisherDTO publisher;
    
    private Set<AuthorDTO> authors;
}
