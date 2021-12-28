package wheresoever.quickprotoserver.domain.randommessage.domain;

import lombok.*;
import wheresoever.quickprotoserver.domain.member.domain.Member;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "random_messages")
@Builder
public class RandomMessage {

    @Id
    @GeneratedValue
    @Column(name = "random_message_id")
    private Long id;

    @Column(name = "canceled_at")
    private LocalDateTime canceledAt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private Member sender;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    private Member receiver;

    private String content;

    private LocalDateTime at;

    private Integer grade;

    /*생성 메서드*/
    public RandomMessage(Member sender, Member receiver, String content) {
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        this.at = LocalDateTime.now();
    }

    public void delete() {
        this.canceledAt = LocalDateTime.now();
    }

    public void grading(int grade) {
        this.grade = grade;
    }
}