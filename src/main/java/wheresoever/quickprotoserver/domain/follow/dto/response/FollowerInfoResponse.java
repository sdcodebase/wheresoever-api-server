package wheresoever.quickprotoserver.domain.follow.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FollowerInfoResponse {

    private String id;
    private String nickname;

    private FollowerInfoResponse(Long followId, String nickname) {
        this.id = followId.toString();
        this.nickname = nickname;
    }

    public static FollowerInfoResponse of(Long followId, String nickname) {
        return new FollowerInfoResponse(followId, nickname);
    }
}
