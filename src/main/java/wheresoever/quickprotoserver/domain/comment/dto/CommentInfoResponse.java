package wheresoever.quickprotoserver.domain.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import wheresoever.quickprotoserver.domain.comment.domain.Comment;
import wheresoever.quickprotoserver.domain.member.domain.Member;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentInfoResponse {
    private String id;
    private String memberId;
    private String nickname;
    private String content;
    private String at;

    private CommentInfoResponse(Comment comment) {
        Member member = comment.getMember();

        this.id = comment.getId().toString();
        this.memberId = member.getId().toString();
        this.nickname = member.getNickname();
        this.content = comment.getContent();
        this.at = comment.getAt().toString();
    }

    public static CommentInfoResponse of(Comment comment) {
        return new CommentInfoResponse(comment);
    }
}
