package wheresoever.quickprotoserver.domain.post.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import wheresoever.quickprotoserver.domain.comment.domain.Comment;
import wheresoever.quickprotoserver.domain.post.domain.Post;

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

    public PostDetailDto(Post post, List<Comment> comments, int likeCount) {
        this.content = post.getContent();
        this.likeCount = Integer.toString(likeCount);
        this.comments = comments
                .stream()
                .map(CommentDto::transfer).collect(Collectors.toList());

        this.nickname = post.getMember().getNickname();
        this.memberId = Long.toString(post.getMember().getId());
        this.category = "예술";
        this.at = post.getAt();
    }

    @Data
    @Builder
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    static class CommentDto {
        private String id;
        private String memberId;
        private String content;
        private LocalDateTime at;

        public static CommentDto transfer(Comment comment) {
            return CommentDto.builder()
                    .id(comment.getId().toString())
                    .content(comment.getContent())
                    .memberId(comment.getMember().getId().toString())
                    .at(comment.getAt())
                    .build();
        }


    }
}


