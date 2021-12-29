package wheresoever.quickprotoserver.domain.post.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import wheresoever.quickprotoserver.domain.post.domain.Post;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PostsResponse {
    private String content;
    private String likeCount;
    private String commentCount;
    private String memberId;
    private String nickName;
    private String id;
    private String category;

    private PostsResponse(Post post) {
        this.content = post.getContent();
        this.likeCount = Long.toString(post.getLikes().size());
        this.commentCount = String.valueOf(post.getComments().size());
        this.memberId = post.getMember().getId().toString();
        this.nickName = post.getMember().getNickname();
        this.id = Long.toString(post.getId());
        this.category = post.getCategory().toString();
    }

    public static PostsResponse of(Post post) {
        return new PostsResponse(post);
    }
}
