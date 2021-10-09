package wheresoever.quickprotoserver.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "members")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private Sex sex;

    private String nickname;

    private LocalDate birthdate;

    private String metropolitan;

    @OneToOne(mappedBy = "member", fetch = FetchType.LAZY)
    private Withdrawn withdrawn;

    public Member(String email, String password, Sex sex, String nickname, LocalDate birthdate, String metropolitan) {
        this.email = email;
        this.password = password;
        this.sex = sex;
        this.nickname = nickname;
        this.birthdate = birthdate;
        this.metropolitan = metropolitan;
    }

    // 연관관계 편의 메서드
    public void withdraw(Withdrawn withdrawn) {
        this.withdrawn = withdrawn;
        withdrawn.setMember(this);
    }

}
