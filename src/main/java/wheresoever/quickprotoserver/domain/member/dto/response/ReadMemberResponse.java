package wheresoever.quickprotoserver.domain.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReadMemberResponse {
    private String sex;
    private String nickname;
    private String birthdate;
    private String metropolitan;
}