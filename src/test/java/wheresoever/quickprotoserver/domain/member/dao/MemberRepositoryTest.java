package wheresoever.quickprotoserver.domain.member.dao;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import wheresoever.quickprotoserver.domain.member.domain.Member;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void findByEmail() {
        //given
        Member member = Member.builder()
                .email("sdcodebase@gmail.com")
                .password("1234")
                .build();

        memberRepository.save(member);

        //when
        Optional<Member> optionalMember = memberRepository.findByEmail(member.getEmail());

        //then
        assertThat(optionalMember.get().getEmail()).isEqualTo("sdcodebase@gmail.com");
    }

    @Test
    void searchMemberByOption() {

        Member m1 = Member.builder()
                .metropolitan("경기")
                .birthdate(LocalDate.MIN)
                .build();

        Member m2 = Member.builder()
                .metropolitan("서울")
                .birthdate(LocalDate.now())
                .build();

        Member m3 = Member.builder()
                .metropolitan("서울")
                .birthdate(LocalDate.now().minusDays(7))
                .build();

        Member m4 = Member.builder()
                .metropolitan("서울")
                .birthdate(LocalDate.now().plusDays(1000))
                .build();

        memberRepository.saveAll(List.of(m1, m2, m3, m4));

        List<Member> members = memberRepository.searchMemberByOption(
                "서울",
                LocalDate.now().minusDays(10),
                LocalDate.now().plusDays(10)
        );

        // 지역이 다르다.
        assertThat(findMemberFromArray(members, m1)).isFalse();

        assertThat(findMemberFromArray(members, m2)).isTrue();
        assertThat(findMemberFromArray(members, m3)).isTrue();

        // 기간에 안 맞는다.
        assertThat(findMemberFromArray(members, m4)).isFalse();
    }

    private boolean findMemberFromArray(List<Member> members, Member member) {
        return members.stream().anyMatch(m -> m.equals(member));
    }
}