package wheresoever.quickprotoserver.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import wheresoever.quickprotoserver.domain.follow.application.FollowService;
import wheresoever.quickprotoserver.domain.member.application.MemberService;
import wheresoever.quickprotoserver.domain.member.domain.Member;
import wheresoever.quickprotoserver.domain.randommessage.application.RandomMessageService;
import wheresoever.quickprotoserver.domain.model.Sex;
import wheresoever.quickprotoserver.domain.member.dto.SearchMemberDto;
import wheresoever.quickprotoserver.domain.member.dao.MemberRepository;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    FollowService followService;

    @Autowired
    RandomMessageService randomMessageService;


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

    @Test
    public void 멤버_검색_특정_점수보다_크거나_같을때() throws Exception {
        //given
        String metro = "서울";
        int grade2 = 2;
        int grade3 = 3;

        Long memberId = memberService.join(new Member("sdkim@gmail.com", "1234", Sex.MALE, "111", LocalDate.now(), metro));
        Long receiverId = memberService.join(new Member("aaa@gmail.com", "1234", Sex.MALE, "222", LocalDate.now(), metro));

        Long member_grade2 = memberService.join(new Member("bbb@gmail.com", "1234", Sex.MALE, "333", LocalDate.now(), metro));
        Long member_grade3 = memberService.join(new Member("ccc@gmail.com", "1234", Sex.MALE, "444", LocalDate.now(), metro));

        Long msgId1 = randomMessageService.send(receiverId, member_grade2, "hello");
        randomMessageService.evaluateMember(receiverId, msgId1, grade2);

        Long msgId2 = randomMessageService.send(receiverId, member_grade3, "hello");
        randomMessageService.evaluateMember(receiverId, msgId2, grade3);

        Long msgId3 = randomMessageService.send(member_grade2, member_grade3, "hh");
        randomMessageService.evaluateMember(member_grade2, msgId3, grade3);

        //when
        List<SearchMemberDto> members = memberService.searchMember(memberId, metro, grade3, LocalDate.MIN, LocalDate.MAX);

        //then
        Assertions.assertThat(members.size()).isEqualTo(1);

        boolean existanceOfMember_grade3 = members.stream()
                .anyMatch(searchMemberDto -> searchMemberDto.getId().equals(member_grade3.toString()));

        Assertions.assertThat(existanceOfMember_grade3).isEqualTo(true);

        boolean existanceOfMember_grade2 = members.stream()
                .anyMatch(searchMemberDto -> searchMemberDto.getId().equals(member_grade2.toString()));

        Assertions.assertThat(existanceOfMember_grade2).isEqualTo(false);
    }

    @Test
    public void 멤버_검색_팔로워_제외() throws Exception {
        //given
        String metro = "서울";
        int grade = 2;
        Long memberId = memberService.join(new Member("sdkim@gmail.com", "1234", Sex.MALE, "111", LocalDate.now(), metro));
        Long receiverId = memberService.join(new Member("aaa@gmail.com", "1234", Sex.MALE, "222", LocalDate.now(), metro));

        Long listMember1 = memberService.join(new Member("bbb@gmail.com", "1234", Sex.MALE, "333", LocalDate.now(), metro));
        Long listMember2 = memberService.join(new Member("ccc@gmail.com", "1234", Sex.MALE, "444", LocalDate.now(), metro));

        Long msgId1 = randomMessageService.send(receiverId, listMember1, "hello");
        randomMessageService.evaluateMember(receiverId, msgId1, grade + 1);

        Long msgId2 = randomMessageService.send(receiverId, listMember2, "hello");
        randomMessageService.evaluateMember(receiverId, msgId2, grade + 1);

        followService.follow(memberId, listMember2);

        //when
        List<SearchMemberDto> members = memberService.searchMember(memberId, metro, grade, LocalDate.MIN, LocalDate.MAX);

        //then
        boolean existanceOfListMember1 = members.stream().anyMatch(searchMemberDto -> searchMemberDto.getId().equals(listMember1.toString()));
        boolean existanceOfListMember2 = members.stream().anyMatch(searchMemberDto -> searchMemberDto.getId().equals(listMember2.toString()));

        Assertions.assertThat(existanceOfListMember1).isEqualTo(true);
        Assertions.assertThat(existanceOfListMember2).isEqualTo(false);
    }

//    @Test
//    public void 멤버_검색_다른지역_제외() throws Exception {
//
//
//    }
}