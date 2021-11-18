package wheresoever.quickprotoserver.repository.follow;

import wheresoever.quickprotoserver.domain.Follow;

import java.util.List;

public interface FollowRepositoryCustom {

    List<Follow> getFollowers(Long memberId);

    List<Follow> getFollowings(Long memberId);

    Follow getFollowByMemberIdAndFollowerId(Long memberId, Long followerId);
}
