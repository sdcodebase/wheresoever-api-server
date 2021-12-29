package wheresoever.quickprotoserver.domain.member.dto.request;


import lombok.*;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class LoginMemberRequest {
    @NotBlank
    private String email;

    @NotBlank
    private String password;
}
