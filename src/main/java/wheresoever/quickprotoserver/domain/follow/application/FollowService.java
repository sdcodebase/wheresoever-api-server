package wheresoever.quickprotoserver.domain.follow.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wheresoever.quickprotoserver.domain.follow.exception.AlreadyFollowedException;
import wheresoever.quickprotoserver.domain.follow.domain.Follow;
import wheresoever.quickprotoserver.domain.member.domain.Member;
import wheresoever.quickprotoserver.domain.follow.dao.FollowRepository;
import wheresoever.quickprotoserver.domain.member.dao.MemberRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FollowService {

    private final FollowRepository followRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Long follow(Long memberId, Long followerId) {
        Optional<Follow> optionalFollow = followRepository.getFollowByMemberIdAndFollowerId(memberId, followerId);
        if (optionalFollow.isPresent()) {
            throw new AlreadyFollowedException();
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
