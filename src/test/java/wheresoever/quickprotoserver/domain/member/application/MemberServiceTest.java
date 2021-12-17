package wheresoever.quickprotoserver.domain.member.application;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import wheresoever.quickprotoserver.domain.member.dao.MemberRepository;
import wheresoever.quickprotoserver.domain.member.domain.Member;
import wheresoever.quickprotoserver.domain.member.exception.MemberEmailPreviousExistsException;
import wheresoever.quickprotoserver.domain.member.exception.MemberNotFoundException;
import wheresoever.quickprotoserver.domain.model.Sex;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    MemberRepository memberRepository;

    @InjectMocks
    MemberService memberService;

    @Test
    void 멤버_찾기_실패() throws Exception {
        given(memberRepository.findById(anyLong())).willReturn(Optional.empty());
        assertThatThrownBy(() -> {
            memberService.findMember(0L);
        }).isInstanceOf(MemberNotFoundException.class);
    }

    @Test
    void 멤버_회원가입() throws Exception {
        //given
        Member member = Member.builder()
                .id(1L)
                .email("sdcodebase@gmail.com")
                .password("1234")
                .build();

        given(memberRepository.findByEmail(anyString())).willReturn(Optional.empty());
        given(memberRepository.save(member)).willReturn(member);

        //when
        Long memberId = memberService.join(member);

        //then
        assertThat(memberId).isEqualTo(1L);
    }

    @Test
    void 멤버_회원가입_중복이름() throws Exception {
        //given
        String email = "sdcodebase@gmail.com";

        Member previousMember = Member.builder()
                .id(1L)
                .email(email)
                .build();

        given(memberRepository.findByEmail(anyString())).willReturn(Optional.of(previousMember));

        //then
        assertThatThrownBy(() -> {
            //when
            Member newMember = Member.builder()
                    .email(email)
                    .build();

            memberService.join(newMember);
        }).isInstanceOf(MemberEmailPreviousExistsException.class);

    }

    @Test
    public void 멤버_정보_수정() throws Exception {
        // given
        Long memberId = 1L;
        Member member = Member.builder()
                .id(memberId)
                .email("sdcodebase@gmail.com")
                .password("1234")
                .sex(Sex.MALE)
                .nickname("김선도")
                .birthdate(LocalDate.now())
                .metropolitan("서울")
                .build();


        given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));

        // when
        memberService.updateMember(memberId, Sex.FEMALE, "aaaa", LocalDate.parse("2014-10-29"), "전남");

        // then
        assertThat(member.getSex()).isEqualTo(Sex.FEMALE);
        assertThat(member.getNickname()).isEqualTo("aaaa");
        assertThat(member.getBirthdate()).isEqualTo("2014-10-29");
        assertThat(member.getMetropolitan()).isEqualTo("전남");
    }
//
//    @Test
//    public void 멤버_검색_특정_점수보다_크거나_같을때() throws Exception {
//        //given
//        String metro = "서울";
//        int grade2 = 2;
//        int grade3 = 3;
//
//        Long memberId = memberService.join(new Member("sdkim@gmail.com", "1234", Sex.MALE, "111", LocalDate.now(), metro));
//        Long receiverId = memberService.join(new Member("aaa@gmail.com", "1234", Sex.MALE, "222", LocalDate.now(), metro));
//
//        Long member_grade2 = memberService.join(new Member("bbb@gmail.com", "1234", Sex.MALE, "333", LocalDate.now(), metro));
//        Long member_grade3 = memberService.join(new Member("ccc@gmail.com", "1234", Sex.MALE, "444", LocalDate.now(), metro));
//
//        Long msgId1 = randomMessageService.send(receiverId, member_grade2, "hello");
//        randomMessageService.evaluateMember(receiverId, msgId1, grade2);
//
//        Long msgId2 = randomMessageService.send(receiverId, member_grade3, "hello");
//        randomMessageService.evaluateMember(receiverId, msgId2, grade3);
//
//        Long msgId3 = randomMessageService.send(member_grade2, member_grade3, "hh");
//        randomMessageService.evaluateMember(member_grade2, msgId3, grade3);
//
//        //when
//        List<SearchMemberDto> members = memberService.searchMember(memberId, metro, grade3, LocalDate.MIN, LocalDate.MAX);
//
//        //then
//        Assertions.assertThat(members.size()).isEqualTo(1);
//
//        boolean existanceOfMember_grade3 = members.stream()
//                .anyMatch(searchMemberDto -> searchMemberDto.getId().equals(member_grade3.toString()));
//
//        Assertions.assertThat(existanceOfMember_grade3).isEqualTo(true);
//
//        boolean existanceOfMember_grade2 = members.stream()
//                .anyMatch(searchMemberDto -> searchMemberDto.getId().equals(member_grade2.toString()));
//
//        Assertions.assertThat(existanceOfMember_grade2).isEqualTo(false);
//    }
//
//    @Test
//    public void 멤버_검색_팔로워_제외() throws Exception {
//        //given
//        String metro = "서울";
//        int grade = 2;
//        Long memberId = memberService.join(new Member("sdkim@gmail.com", "1234", Sex.MALE, "111", LocalDate.now(), metro));
//        Long receiverId = memberService.join(new Member("aaa@gmail.com", "1234", Sex.MALE, "222", LocalDate.now(), metro));
//
//        Long listMember1 = memberService.join(new Member("bbb@gmail.com", "1234", Sex.MALE, "333", LocalDate.now(), metro));
//        Long listMember2 = memberService.join(new Member("ccc@gmail.com", "1234", Sex.MALE, "444", LocalDate.now(), metro));
//
//        Long msgId1 = randomMessageService.send(receiverId, listMember1, "hello");
//        randomMessageService.evaluateMember(receiverId, msgId1, grade + 1);
//
//        Long msgId2 = randomMessageService.send(receiverId, listMember2, "hello");
//        randomMessageService.evaluateMember(receiverId, msgId2, grade + 1);
//
//        followService.follow(memberId, listMember2);
//
//        //when
//        List<SearchMemberDto> members = memberService.searchMember(memberId, metro, grade, LocalDate.MIN, LocalDate.MAX);
//
//        //then
//        boolean existanceOfListMember1 = members.stream().anyMatch(searchMemberDto -> searchMemberDto.getId().equals(listMember1.toString()));
//        boolean existanceOfListMember2 = members.stream().anyMatch(searchMemberDto -> searchMemberDto.getId().equals(listMember2.toString()));
//
//        Assertions.assertThat(existanceOfListMember1).isEqualTo(true);
//        Assertions.assertThat(existanceOfListMember2).isEqualTo(false);
//    }

//    @Test
//    public void 멤버_검색_다른지역_제외() throws Exception {
//
//
//    }
}