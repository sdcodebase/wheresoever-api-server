package wheresoever.quickprotoserver.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "members")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @Column(name = "canceled_at")
    private LocalDateTime canceledAt;


    @OneToMany(mappedBy = "member")
    private List<Post> posts = new ArrayList<>();

    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private Sex sex;

    private String nickname;

    private LocalDate birthdate;

    private String metropolitan;

    public Member(String email, String password, Sex sex, String nickname, LocalDate birthdate, String metropolitan) {
        this.email = email;
        this.password = password;
        this.sex = sex;
        this.nickname = nickname;
        this.birthdate = birthdate;
        this.metropolitan = metropolitan;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public void setMetropolitan(String metropolitan) {
        this.metropolitan = metropolitan;
    }

}
