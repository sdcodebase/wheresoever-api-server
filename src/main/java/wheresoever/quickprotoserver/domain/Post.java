package wheresoever.quickprotoserver.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "posts")
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
