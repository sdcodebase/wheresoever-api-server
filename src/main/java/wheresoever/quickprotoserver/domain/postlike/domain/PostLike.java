package wheresoever.quickprotoserver.domain.postlike.domain;

import lombok.*;
import wheresoever.quickprotoserver.domain.member.domain.Member;
import wheresoever.quickprotoserver.domain.post.domain.Post;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "post_likes")
@Builder
public class PostLike {

    @Id
    @GeneratedValue
    @Column(name = "post_like_id")
    private Long id;

    @Column(name = "canceled_at")
    private LocalDateTime canceledAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private LocalDateTime at;

    /* 생성 매서드 */
    public PostLike(Post post, Member member) {
        this.post = post;
        this.post.getLikes().add(this);
        this.member = member;
        this.at = LocalDateTime.now();
    }

    public void unlike() {
        this.canceledAt = LocalDateTime.now();
    }

    public void undoDislike() {
        this.canceledAt = null;
    }
}
