package wheresoever.quickprotoserver.domain.post.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import wheresoever.quickprotoserver.domain.member.domain.Member;
import wheresoever.quickprotoserver.domain.post.domain.Post;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class PostDetailDto {
    private String id;
    private String content;
    private String likeCount;

    private String memberId;
    private String nickname;
    private String category;
    private LocalDateTime at;

    private PostDetailDto(Post post, Integer likeCount) {
        Member member = post.getMember();

        this.id = post.getId().toString();
        this.content = post.getContent();
        this.likeCount = likeCount.toString();
        this.nickname = member.getNickname();
        this.memberId = member.getId().toString();
        this.category = "예술";
        this.at = post.getAt();
    }

    public static PostDetailDto of(Post post, Integer likeCount) {
        return new PostDetailDto(post, likeCount);
    }
}


