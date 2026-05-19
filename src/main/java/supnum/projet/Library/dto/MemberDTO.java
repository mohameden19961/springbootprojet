package supnum.projet.Library.dto;

import lombok.*;
import jakarta.validation.constraints.*;
import supnum.projet.Library.data.enums.MemberType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberDTO {
    private Long id;
    
    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    private String email;
    
    @NotNull(message = "Member type is required")
    private MemberType memberType;
    
    @Positive(message = "Max borrows must be positive")
    @NotNull(message = "Max borrows is required")
    private Integer maxBorrows;
}
