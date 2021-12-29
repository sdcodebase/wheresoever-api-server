package wheresoever.quickprotoserver.domain.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateMemberResponse<T> {
    private T token;
}
