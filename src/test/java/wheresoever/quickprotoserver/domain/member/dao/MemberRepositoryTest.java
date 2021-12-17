package wheresoever.quickprotoserver.domain.member.dao;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import wheresoever.quickprotoserver.domain.member.domain.Member;

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
}