package supnum.projet.Library.dto;

import supnum.projet.Library.data.entities.enums.MemberType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class MemberDTO {
    private Long id;

    @NotBlank(message = "L'email est requis")
    @Email(message = "Email invalide")
    private String email;

    @NotNull(message = "Le type de membre est requis")
    private MemberType memberType;

    @NotNull(message = "Le nombre maximum d'emprunts est requis")
    @Positive(message = "Le maximum d'emprunts doit être positif")
    private Integer maxBorrows;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public MemberType getMemberType() { return memberType; }
    public void setMemberType(MemberType memberType) { this.memberType = memberType; }
    public Integer getMaxBorrows() { return maxBorrows; }
    public void setMaxBorrows(Integer maxBorrows) { this.maxBorrows = maxBorrows; }
}
