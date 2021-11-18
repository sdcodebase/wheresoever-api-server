package wheresoever.quickprotoserver.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wheresoever.quickprotoserver.domain.Follow;
import wheresoever.quickprotoserver.domain.Member;
import wheresoever.quickprotoserver.repository.follow.FollowRepository;
import wheresoever.quickprotoserver.repository.member.MemberRepository;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FollowService {

    private final FollowRepository followRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Long follow(Long memberId, Long followerId) {
        Follow prevFollowed = followRepository.getFollowByMemberIdAndFollowerId(memberId, followerId);
        if (Objects.nonNull(prevFollowed)) {
            throw new IllegalStateException("이미 팔로우했습니다.");
        }


        Member member = memberRepository.findById(memberId).get();
        Member follower = memberRepository.findById(followerId).get();

        Follow follow = new Follow(member, follower);

        return followRepository.save(follow).getId();
    }

    public List<Follow> getFollowers(Long memberId) {
        return followRepository.getFollowers(memberId);
    }

    public List<Follow> getFollowings(Long memberId) {
        return followRepository.getFollowings(memberId);
    }
}
