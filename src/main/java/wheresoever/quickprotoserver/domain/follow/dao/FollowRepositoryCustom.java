package wheresoever.quickprotoserver.domain.follow.dao;

import wheresoever.quickprotoserver.domain.follow.domain.Follow;

import java.util.HashMap;
import java.util.List;

public interface FollowRepositoryCustom {

    List<Follow> getFollowers(Long memberId);

    List<Follow> getFollowings(Long memberId);

    Follow getFollowByMemberIdAndFollowerId(Long memberId, Long followerId);

    HashMap<Long, Integer> getFollowerCountMap(List<Long> memberIds);

    //특정 멤버를 memberIds들이 follow하는지 판단해주는 map
    HashMap<Long, Boolean> getMemberFollowingMap(Long memberId, List<Long> memberIds);
}
