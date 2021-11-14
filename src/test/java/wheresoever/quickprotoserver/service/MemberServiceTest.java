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
        Member m1 = new Member("sdkim@gmail.com", "1234", Sex.MALE, "sundo", LocalDate.now(), "서울");

        //when
        Long m1Id = memberService.join(m1);


        //then
        Member m2 = memberRepository.findById(m1Id).get();
        Assertions.assertThat(m1).isEqualTo(m2);
    }

    @Test
    public void 멤버_회원가입_중복이름() throws Exception {
        //given
        Member m1 = new Member("sdkim@gmail.com", "1234", Sex.MALE, "sundo", LocalDate.now(), "서울");
        Long memberId = memberService.join(m1);

        //when
        Member m2 = new Member("sdkim@gmail.com", "111", Sex.FEMALE, "sdsd", LocalDate.now(), "경기");

        //then
        assertThrows(IllegalStateException.class, () -> {
            memberService.join(m2); //예외발생
        });
    }

    @Test
    public void 멤버_정보_수정() throws Exception {
        // given
        Member m1 = new Member("sdkim@gmail.com", "1234", Sex.MALE, "sundo", LocalDate.now(), "서울");
        Long memberId = memberService.join(m1);

        // when
        memberService.updateMember(memberId, Sex.FEMALE, "aaaa", LocalDate.parse("2014-10-29"), "전남");

        // then
        Member actual = memberService.findMember(memberId);
        assertEquals(Sex.FEMALE, actual.getSex());
        assertEquals("aaaa", actual.getNickname());
        assertEquals(LocalDate.parse("2014-10-29"), actual.getBirthdate());
        assertEquals("전남", actual.getMetropolitan());

    }
}