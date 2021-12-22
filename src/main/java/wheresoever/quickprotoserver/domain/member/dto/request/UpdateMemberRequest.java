package wheresoever.quickprotoserver.domain.member.dto.request;

import lombok.*;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UpdateMemberRequest {
    private String sex;
    private String nickname;
    private String birthdate;
    private String metropolitan;
}
