package wheresoever.quickprotoserver.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "members")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String username;
    private int age;
    private String introduce;


    public Member(String username, int age, String introduce) {
        this.username = username;
        this.age = age;
        this.introduce = introduce;
    }
}
