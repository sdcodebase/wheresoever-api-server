package wheresoever.quickprotoserver.domain.member.dto.request;


import lombok.*;

import javax.validation.constraints.NotEmpty;


@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateMemberRequest {
    @NotEmpty
    private String email;
    @NotEmpty
    private String password;

    private String sex;
    private String birthdate;
    private String nickname;
    private String metropolitan;
}
