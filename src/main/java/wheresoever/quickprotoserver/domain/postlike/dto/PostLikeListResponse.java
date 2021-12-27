package wheresoever.quickprotoserver.domain.postlike.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import wheresoever.quickprotoserver.domain.member.domain.Member;
import wheresoever.quickprotoserver.domain.postlike.domain.PostLike;

import static com.fasterxml.jackson.annotation.JsonInclude.Include;

@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
@Data
public class PostLikeListResponse {
    private String nickname;
    private String memberId;

    public static PostLikeListResponse from(PostLike like) {
        Member member = like.getMember();
        String nickname = member.getNickname();
        String memberId = member.getId().toString();

        return new PostLikeListResponse(nickname, memberId);
    }
}
