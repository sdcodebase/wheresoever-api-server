package wheresoever.quickprotoserver.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import wheresoever.quickprotoserver.domain.Member;
import wheresoever.quickprotoserver.repository.member.MemberRepository;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @Test
    public void 멤버_회원가입() throws Exception{
        //given
        String username = "kim";
        int age = 25;
        String introduce = "안녕하세요";

        Member member = new Member(username, age, introduce);

        //when
        Long id = memberService.join(member);

        //then
        Assertions.assertThat(member).isEqualTo(memberRepository.findById(id).get());
    }


    @Test
    public void 멤버_회원가입_중복이름() throws Exception{
        //given
        String username = "kim";
        int age = 25;
        String introduce = "안녕하세요";

        Member member1 = new Member(username, age, introduce);
        Long member1Id = memberService.join(member1);

        //when
        Member member2 = new Member(username, age, introduce);

        //then
        assertThrows(IllegalStateException.class, ()->{
            memberService.join(member2); //예외발생
        });
    }
}