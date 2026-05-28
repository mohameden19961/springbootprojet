package supnum.projet.Library.dto.response;

import supnum.projet.Library.data.entities.enums.MemberType;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MemberResponse {
    private Long id;
    private String email;
    private MemberType memberType;
    private Integer maxBorrows;
}
