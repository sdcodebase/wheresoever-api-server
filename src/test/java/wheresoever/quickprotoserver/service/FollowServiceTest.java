package wheresoever.quickprotoserver.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import wheresoever.quickprotoserver.domain.Follow;
import wheresoever.quickprotoserver.domain.Member;
import wheresoever.quickprotoserver.domain.Sex;
import wheresoever.quickprotoserver.repository.follow.FollowRepository;
import wheresoever.quickprotoserver.repository.member.MemberRepository;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@SpringBootTest
@Transactional
class FollowServiceTest {

    @Autowired
    private FollowService followService;

    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    EntityManager em;

    @Test
    public void 팔로우하기() throws Exception {
        //given
        Member member = memberRepository.save(new Member("sdkim@gmail.com", "1234", Sex.MALE, "sundo", LocalDate.now(), "서울"));
        Member follower = memberRepository.save(new Member("aaa@gmail.com", "1234", Sex.MALE, "sda", LocalDate.now(), "서울"));

        //when
        Long followId = followService.follow(member.getId(), follower.getId());

        //then
        Follow follow = followRepository.findById(followId).get();

        assertThat(follow.getFollower().getId()).isEqualTo(follower.getId());
        assertThat(follow.getMember().getId()).isEqualTo(member.getId());
    }

    @Test
    public void 이미_팔로우_했을때() throws Exception {
        //given
        Member member = memberRepository.save(new Member("sdkim@gmail.com", "1234", Sex.MALE, "sundo", LocalDate.now(), "서울"));
        Member follower = memberRepository.save(new Member("aaa@gmail.com", "1234", Sex.MALE, "sda", LocalDate.now(), "서울"));

        followService.follow(member.getId(), follower.getId());

        //then
        assertThatThrownBy(() -> {
            //when
            followService.follow(member.getId(), follower.getId());
        }).isInstanceOf(IllegalStateException.class);
    }

    @Test
    public void 팔로워_조회() throws Exception {
        //given
        Member member = memberRepository.save(new Member("sdkim@gmail.com", "1234", Sex.MALE, "sundo", LocalDate.now(), "서울"));
        Member follower1 = memberRepository.save(new Member("aaa@gmail.com", "1234", Sex.MALE, "sda", LocalDate.now(), "서울"));
        Member follower2 = memberRepository.save(new Member("bbb@gmail.com", "1234", Sex.MALE, "sda", LocalDate.now(), "서울"));

        followService.follow(member.getId(), follower1.getId());
        followService.follow(member.getId(), follower2.getId());

        //when
        List<Follow> followers = followService.getFollowers(member.getId());

        //then
        List<String> emails = Arrays.asList(follower2.getEmail(), follower1.getEmail());
        for (int i = 0; i < followers.size(); i++) {
            Follow follow = followers.get(i);
            assertThat(follow.getFollower().getEmail()).isEqualTo(emails.get(i));
        }
    }


    @Test
    public void 팔로잉_조회() throws Exception {
        //given
        Member member1 = memberRepository.save(new Member("sdkim@gmail.com", "1234", Sex.MALE, "sundo", LocalDate.now(), "서울"));
        Member member2 = memberRepository.save(new Member("aaa@gmail.com", "1234", Sex.MALE, "sda", LocalDate.now(), "서울"));
        Member follower = memberRepository.save(new Member("bbb@gmail.com", "1234", Sex.MALE, "sda", LocalDate.now(), "서울"));

        //when
        followService.follow(member1.getId(), follower.getId());
        followService.follow(member2.getId(), follower.getId());

        //then
        List<Follow> followings = followService.getFollowings(follower.getId());
    }
}