package wheresoever.quickprotoserver.domain.member.application;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import wheresoever.quickprotoserver.domain.follow.dao.FollowRepository;
import wheresoever.quickprotoserver.domain.member.dao.MemberRepository;
import wheresoever.quickprotoserver.domain.member.domain.Member;
import wheresoever.quickprotoserver.domain.member.dto.SearchMemberDto;
import wheresoever.quickprotoserver.domain.member.exception.MemberEmailPreviousExistsException;
import wheresoever.quickprotoserver.domain.member.exception.MemberNotFoundException;
import wheresoever.quickprotoserver.domain.model.Sex;
import wheresoever.quickprotoserver.domain.randommessage.dao.RandomMessageRepository;
import wheresoever.quickprotoserver.domain.randommessage.domain.RandomMessage;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    MemberRepository memberRepository;

    @Mock
    RandomMessageRepository randomMessageRepository;

    @Mock
    FollowRepository followRepository;

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

    @Test
    void 멤버_검색_특정_점수보다_크거나_같을때() {
        //given
        Member member1 = Member.builder()
                .id(1L)
                .build();

        Member member2 = Member.builder()
                .id(2L)
                .build();

        Member member3 = Member.builder()
                .id(3L)
                .build();

        // 리스팅될 대상만 필드 SET
        Member member4 = Member.builder()
                .id(4L)
                .email("sdcodebase@gmail.com")
                .birthdate(LocalDate.now())
                .metropolitan("서울")
                .nickname("호호")
                .sex(Sex.FEMALE)
                .build();

        List<Member> members = List.of(member1, member2, member3, member4);
        given(memberRepository.searchMemberByOption(anyString(), any(LocalDate.class), any(LocalDate.class)))
                .willReturn(members);


        RandomMessage message1 = RandomMessage.builder()
                .sender(member2)
                .grade(2)
                .build();

        RandomMessage message2 = RandomMessage.builder()
                .sender(member3)
                .grade(4)
                .build();

        RandomMessage message3 = RandomMessage.builder()
                .sender(member4)
                .grade(5)
                .build();

        List<RandomMessage> messages = List.of(message1, message2, message3);
        given(randomMessageRepository.findRandomMessagesByGradeIsNotNullAndSenderIn(anyList()))
                .willReturn(messages);


        HashMap<Long, Boolean> memberFollowingMap = new HashMap<>();
        members.forEach(member -> memberFollowingMap.put(member.getId(), false));
        memberFollowingMap.put(member3.getId(), true); //member3는 member1 팔로우한다
        given(followRepository.getMemberFollowingMap(anyLong(), anyList()))
                .willReturn(memberFollowingMap);


        HashMap<Long, Integer> followerCountMap = new HashMap<>();
        members.forEach(member -> followerCountMap.put(member.getId(), 0)); // 메타데이터는 테스트와 무관하여 임의 생성

        //when
        List<SearchMemberDto> searchMemberDtos = memberService.searchMember(member1.getId(), "서울", 4, LocalDate.MIN, LocalDate.MAX);

        //then

        // 기준 평점보다 낮아서 평점으로 제외
        boolean member2Existence = checkMemberDtoExistenceById(searchMemberDtos, member2.getId());
        assertThat(member2Existence).isFalse();

        // 팔로워 제외
        boolean member3Existence = checkMemberDtoExistenceById(searchMemberDtos, member3.getId());
        assertThat(member3Existence).isFalse();

        boolean member4Existence = checkMemberDtoExistenceById(searchMemberDtos, member4.getId());
        assertThat(member4Existence).isTrue();
    }

    private boolean checkMemberDtoExistenceById(List<SearchMemberDto> dtos, Long memberId) {
        return dtos.stream().anyMatch(dto -> dto.getId().equals(Long.toString(memberId)));
    }
}