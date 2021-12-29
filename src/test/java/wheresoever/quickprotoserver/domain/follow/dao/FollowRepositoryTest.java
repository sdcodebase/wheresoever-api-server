package wheresoever.quickprotoserver.domain.follow.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import wheresoever.quickprotoserver.domain.follow.domain.Follow;
import wheresoever.quickprotoserver.domain.member.dao.MemberRepository;
import wheresoever.quickprotoserver.domain.member.domain.Member;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class FollowRepositoryTest {

    @Autowired
    EntityManagerFactory emf;

    @Autowired
    EntityManager em;

    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Member generateEmptyMember() {
        return Member.builder().build();
    }

    @Test
    void getFollowers() {
        //given
        Member member = generateEmptyMember();
        Member follower1 = generateEmptyMember();
        Member follower2 = generateEmptyMember();

        memberRepository.saveAll(List.of(member, follower1, follower2));

        Follow follow1 = new Follow(member, follower1);
        Follow follow2 = new Follow(member, follower2);

        followRepository.saveAll(List.of(follow1, follow2));

        em.flush();
        em.clear();

        //when
        List<Follow> followers = followRepository.getFollowers(member.getId());

        //then
        Follow first = followers.get(0);
        Follow second = followers.get(1);

        assertThat(first.getAt()).isAfter(second.getAt());

        boolean isFetched = emf.getPersistenceUnitUtil().isLoaded(first.getFollower());
        assertThat(isFetched).isTrue();

    }

    @Test
    void getFollowings() {
        //given
        Member follower = generateEmptyMember();
        Member member1 = generateEmptyMember();
        Member member2 = generateEmptyMember();

        memberRepository.saveAll(List.of(follower, member1, member2));

        Follow follow1 = new Follow(member1, follower);
        Follow follow2 = new Follow(member2, follower);

        followRepository.saveAll(List.of(follow1, follow2));

        em.flush();
        em.clear();

        //when
        List<Follow> followers = followRepository.getFollowings(follower.getId());

        //then
        Follow first = followers.get(0);
        Follow second = followers.get(1);

        assertThat(first.getAt()).isAfter(second.getAt());

        boolean isFetched = emf.getPersistenceUnitUtil().isLoaded(first.getMember());
        assertThat(isFetched).isTrue();

    }

    @Test
    void getFollowerCountMap() {
        //given
        Member member1 = generateEmptyMember();
        Member m1Follower1 = generateEmptyMember();
        Member m1Follower2 = generateEmptyMember();

        Member member2 = generateEmptyMember();
        Member m2Follower1 = generateEmptyMember();

        Member member3 = generateEmptyMember();

        List<Member> members = List.of(
                member1, m1Follower1, m1Follower2
                , member2, m2Follower1,
                member3);

        memberRepository.saveAll(members);

        // member1의 팔로워 2명
        Follow f1 = new Follow(member1, m1Follower1);
        Follow f2 = new Follow(member1, m1Follower2);

        // member2의 팔로워 1명
        Follow f3 = new Follow(member2, m2Follower1);

        //member3의 팔로워 없음

        followRepository.saveAll(List.of(f1, f2, f3));

        em.flush();
        em.clear();

        //when
        HashMap<Long, Integer> followerCountMap = followRepository.getFollowerCountMap(members.stream().map(Member::getId).collect(Collectors.toList()));

        //then
        assertThat(followerCountMap.get(member1.getId())).isEqualTo(2);
        assertThat(followerCountMap.get(member2.getId())).isEqualTo(1);
        assertThat(followerCountMap.get(member3.getId())).isNull();
    }

    @Test
    void getMemberFollowingMap() {
        //given
        Member member1 = generateEmptyMember();
        Member m1Follower1 = generateEmptyMember();
        Member member2 = generateEmptyMember();

        memberRepository.saveAll(List.of(member1, m1Follower1, member2));

        //member1의 팔로워: m1Follower1
        Follow f = new Follow(member1, m1Follower1);

        //member2는 member1을 팔로잉하지 않는다.

        followRepository.save(f);

        em.flush();
        em.clear();

        //when
        HashMap<Long, Boolean> memberFollowingMap = followRepository.getMemberFollowingMap(member1.getId(), List.of(m1Follower1.getId(), member2.getId()));

        //then
        assertThat(memberFollowingMap.get(m1Follower1.getId())).isTrue();
        assertThat(memberFollowingMap.get(member2.getId())).isFalse();
    }
}