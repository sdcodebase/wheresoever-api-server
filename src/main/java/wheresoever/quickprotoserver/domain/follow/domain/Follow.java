package wheresoever.quickprotoserver.domain.follow.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import wheresoever.quickprotoserver.domain.member.domain.Member;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "follow")
public class Follow {
    @Id
    @GeneratedValue
    @Column(name = "follow_id")
    private Long id;

    @Column(name = "canceled_at")
    private LocalDateTime canceledAt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower_id")
    private Member follower; // member를 팔로우 하는 사람

    private LocalDateTime at;

    public Follow(Member member, Member follower) {
        this.member = member;
        this.follower = follower;
        this.at = LocalDateTime.now();
    }
}