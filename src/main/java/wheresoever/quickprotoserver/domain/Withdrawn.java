package wheresoever.quickprotoserver.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Withdrawn {

    @Id
    @GeneratedValue
    @Column(name = "withdrawn_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    private String reason;
    private LocalDateTime at;

    public Withdrawn(Member member, String reason) {
        this.member = member;
        this.reason = reason;
        this.at = LocalDateTime.now();
    }
}
