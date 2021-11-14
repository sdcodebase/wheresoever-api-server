package wheresoever.quickprotoserver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import wheresoever.quickprotoserver.domain.Post;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class PostDetailDto {
    private String content;
    private String likeCount;

    private List<CommentDto> comments;

    private String memberId;
    private String nickname;
    private String category;
    private LocalDateTime at;

    public PostDetailDto(Post post, int likeCount) {
        this.content = post.getContent();
        this.likeCount = Integer.toString(likeCount);
        this.comments = post.getComments()
                .stream()
                .map(comment -> new CommentDto(
                                Long.toString(comment.getId()),
                                Long.toString(comment.getMember().getId()),
                                comment.getContent(), comment.getAt(),
                                comment.getCommentChildren()
                                        .stream()
                                        .map(child -> new CommentChildDto(
                                                Long.toString(child.getId()),
                                                Long.toString(child.getMember().getId()),
                                                child.getMember().getNickname(),
                                                child.getAt(),
                                                child.getContent()
                                        ))
                                        .collect(Collectors.toList())
                        )
                ).collect(Collectors.toList());

        this.nickname = post.getMember().getNickname();
        this.memberId = Long.toString(post.getMember().getId());
        this.category = "예술";
//        this.category = post.getCategory();
        this.at = post.getAt();
    }

    @Data
    @AllArgsConstructor
    static class CommentDto {
        private String id;
        private String memberId;
        private String content;
        private LocalDateTime at;
        private List<CommentChildDto> children;
    }

    @Data
    @AllArgsConstructor
    static class CommentChildDto {
        private String id;
        private String memberId;
        private String nickname;
        private LocalDateTime at;
        private String content;
    }
}


