package wheresoever.quickprotoserver.domain.comment.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import wheresoever.quickprotoserver.domain.commentchild.domain.CommentChild;
import wheresoever.quickprotoserver.domain.post.domain.Post;
import wheresoever.quickprotoserver.domain.member.domain.Member;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "comments")
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

    @OneToMany(mappedBy = "comment")
    private List<CommentChild> commentChildren = new ArrayList<>();

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
