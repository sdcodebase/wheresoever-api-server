package wheresoever.quickprotoserver.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import wheresoever.quickprotoserver.domain.Member;
import wheresoever.quickprotoserver.domain.Sex;
import wheresoever.quickprotoserver.repository.member.MemberRepository;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @Test
    public void 멤버_회원가입() throws Exception {
        //given
        Member m1 = new Member("sdkim@gmail.com", "1234", Sex.MALE, "sundo", LocalDate.now(), "서울특별시");

        //when
        Long m1Id = memberService.join(m1);


        //then
        Member m2 = memberRepository.findById(m1Id).get();
        Assertions.assertThat(m1).isEqualTo(m2);
    }

    @Test
    public void 멤버_회원가입_중복이름() throws Exception {
        //given
        Member m1 = new Member("sdkim@gmail.com", "1234", Sex.MALE, "sundo", LocalDate.now(), "서울특별시");
        Long memberId = memberService.join(m1);

        //when
        Member m2 = new Member("sdkim@gmail.com", "111", Sex.FEMALE, "sdsd", LocalDate.now(), "경기도");

        //then
        assertThrows(IllegalStateException.class, () -> {
            memberService.join(m2); //예외발생
        });
    }

    @Test
    public void 멤버_탈퇴() throws Exception {
        //given
        Member m1 = new Member("sdkim@gmail.com", "1234", Sex.MALE, "sundo", LocalDate.now(), "서울특별시");
        Long memberId = memberService.join(m1);

        //when
        Boolean aBoolean = memberService.withdrawnMember(memberId, "그냥");
        Member withdrawnMember = memberRepository.findById(memberId).get();

        //then
        Assertions.assertThat(withdrawnMember.getWithdrawn().getMember()).isEqualTo(m1);
    }
}