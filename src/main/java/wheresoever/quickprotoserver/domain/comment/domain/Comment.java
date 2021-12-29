package wheresoever.quickprotoserver.domain.comment.domain;

import lombok.*;
import wheresoever.quickprotoserver.domain.member.domain.Member;
import wheresoever.quickprotoserver.domain.post.domain.Post;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "comments")
@Builder
public class Comment {

    @Id
    @GeneratedValue
    @Column(name = "comment_id")
    private Long id;

    @Column(name = "canceled_at")
    private LocalDateTime canceledAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    private String content;

    private LocalDateTime at;

    /*생성 메서드*/
    public Comment(Member member, Post post, String content) {
        this.member = member;

        this.post = post;
        post.getComments().add(this);

        this.content = content;
        this.at = LocalDateTime.now();
    }

    public void recomment(String content) {
        this.content = content;
    }

    public void delete() {
        this.canceledAt = LocalDateTime.now();
    }
}
