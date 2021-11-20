package wheresoever.quickprotoserver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import wheresoever.quickprotoserver.domain.Member;

import java.util.Objects;

@Data
@AllArgsConstructor
public class SearchMemberDto {
    private String id;
    private String sex;
    private String nickname;
    private String birthdate;
    private String metropolitan;
    private String grade;
    private String followers;

    public SearchMemberDto(Member member, Double grade, Integer followers) {
        this.id = Long.toString(member.getId());
        this.sex = "남성";
        this.nickname = member.getNickname();
        this.birthdate = member.getBirthdate().toString();
        this.metropolitan = member.getMetropolitan();
        this.grade = Double.toString(grade);
        this.followers = Objects.nonNull(followers) ? Integer.toString(followers) : "0";
    }
}
