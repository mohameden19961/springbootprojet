package supnum.projet.Library.dto;

import lombok.*;
import jakarta.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthorDTO {
    private Long id;
    
    @NotBlank(message = "Author name is required")
    private String name;
    
    private String nationalityCode;
    private NationalityDTO nationality;
}
