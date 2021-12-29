package wheresoever.quickprotoserver.domain.follow.application;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import wheresoever.quickprotoserver.domain.follow.exception.AlreadyFollowedException;
import wheresoever.quickprotoserver.domain.follow.dao.FollowRepository;
import wheresoever.quickprotoserver.domain.follow.domain.Follow;
import wheresoever.quickprotoserver.domain.member.dao.MemberRepository;
import wheresoever.quickprotoserver.domain.member.domain.Member;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class FollowServiceTest {

    @Mock
    private FollowRepository followRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private FollowService followService;

    @Test
    void 팔로우하기() {
        //given
        Member member = Member.builder()
                .id(1L)
                .build();

        Member follower = Member.builder()
                .id(2L)
                .build();

        given(followRepository.getFollowByMemberIdAndFollowerId(anyLong(), anyLong())).willReturn(Optional.empty());
        given(memberRepository.findById(member.getId())).willReturn(Optional.of(member));
        given(memberRepository.findById(follower.getId())).willReturn(Optional.of(follower));

        Follow follow = Follow.builder()
                .id(3L)
                .member(member)
                .follower(follower)
                .build();

        given(followRepository.save(any())).willReturn(follow);

        //when
        Long followId = followService.follow(member.getId(), follower.getId());

        //then
        assertThat(followId).isEqualTo(follow.getId());
    }

    @Test
    void 이미_팔로우_했을때() {
        //given
        given(followRepository.getFollowByMemberIdAndFollowerId(anyLong(), anyLong()))
                .willReturn(Optional.of(Follow.builder().build()));

        //then
        assertThatThrownBy(() -> {
            //when
            followService.follow(anyLong(), anyLong());
        }).isInstanceOf(AlreadyFollowedException.class);
    }
}