package wheresoever.quickprotoserver.domain.post.domain;

import lombok.*;
import wheresoever.quickprotoserver.domain.postlike.domain.PostLike;
import wheresoever.quickprotoserver.domain.comment.domain.Comment;
import wheresoever.quickprotoserver.domain.member.domain.Member;
import wheresoever.quickprotoserver.domain.model.Category;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "posts")
@Builder
public class Post {

    @Id
    @GeneratedValue
    @Column(name = "post_id")
    private Long id;

    @Column(name = "canceled_at")
    private LocalDateTime canceledAt;

    @OneToMany(mappedBy = "post")
    private List<PostLike> likes = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "post")
    private List<Comment> comments = new ArrayList<>();

    private String content;

    @Enumerated(EnumType.STRING)
    private Category category;

    private LocalDateTime at;

    /*생성 메서드*/
    public Post(Member member, String content, Category category) {
        this.member = member;
        this.content = content;
        this.category = category;
        this.at = LocalDateTime.now();
    }

    public void delete() {
        this.canceledAt = LocalDateTime.now();
    }
}
