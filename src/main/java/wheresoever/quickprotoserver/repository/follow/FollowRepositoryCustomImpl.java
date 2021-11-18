package wheresoever.quickprotoserver.repository.follow;

import com.querydsl.jpa.impl.JPAQueryFactory;
import wheresoever.quickprotoserver.domain.Follow;
import wheresoever.quickprotoserver.domain.QFollow;

import javax.persistence.EntityManager;
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
}
