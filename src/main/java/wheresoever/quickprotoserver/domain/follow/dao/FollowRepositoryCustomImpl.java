package wheresoever.quickprotoserver.domain.follow.dao;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import wheresoever.quickprotoserver.domain.follow.domain.Follow;
import wheresoever.quickprotoserver._domain.QFollow;

import javax.persistence.EntityManager;
import java.util.HashMap;
import java.util.List;

public class FollowRepositoryCustomImpl implements FollowRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    private final QFollow follow = QFollow.follow;

    public FollowRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Follow> getFollowers(Long memberId) {
        return queryFactory
                .selectFrom(follow)
                .where(follow.member.id.eq(memberId))
                .join(follow.follower).fetchJoin()
                .orderBy(follow.at.desc())
                .fetch();
    }

    @Override
    public List<Follow> getFollowings(Long memberId) {
        return queryFactory
                .selectFrom(follow)
                .where(follow.follower.id.eq(memberId))
                .join(follow.member).fetchJoin()
                .orderBy(follow.at.desc())
                .fetch();
    }

    @Override
    public Follow getFollowByMemberIdAndFollowerId(Long memberId, Long followerId) {
        return queryFactory
                .selectFrom(follow)
                .where(follow.follower.id.eq(followerId), follow.member.id.eq(memberId))
                .fetchOne();
    }

    @Override
    public HashMap<Long, Integer> getFollowerCountMap(List<Long> memberIds) {
        List<Tuple> followCountTuple = queryFactory
                .select(follow.member.id, follow.count())
                .from(follow)
                .where(follow.member.id.in(memberIds))
                .groupBy(follow.member.id)
                .fetch();

        HashMap<Long, Integer> followerCountMap = new HashMap<>();
        followCountTuple.forEach(tuple -> {
            Long memberId = tuple.get(follow.member.id);

            Integer followerCount = Math.toIntExact(tuple.get(follow.count()));
            followerCountMap.put(memberId, followerCount);
        });
        return followerCountMap;
    }

    @Override
    public HashMap<Long, Boolean> getMemberFollowingMap(Long memberId, List<Long> memberIds) {

        HashMap<Long, Boolean> memberFollowingMap = new HashMap<>();
        memberIds.forEach(id -> memberFollowingMap.put(id, false));

        List<Long> followerMemberIds = queryFactory
                .select(follow.follower.id)
                .from(follow)
                .where(follow.member.id.eq(memberId), follow.follower.id.in(memberIds))
                .fetch();

        followerMemberIds
                .forEach(followerId -> memberFollowingMap.put(followerId, true));

        return memberFollowingMap;
    }
}
