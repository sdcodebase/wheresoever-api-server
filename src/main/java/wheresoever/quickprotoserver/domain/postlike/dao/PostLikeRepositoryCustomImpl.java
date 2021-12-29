package wheresoever.quickprotoserver.domain.postlike.dao;

import com.querydsl.jpa.impl.JPAQueryFactory;
import wheresoever.quickprotoserver.domain.postlike.domain.PostLike;
import wheresoever.quickprotoserver.domain.postlike.domain.QPostLike;

import javax.persistence.EntityManager;
import java.util.List;

public class PostLikeRepositoryCustomImpl implements PostLikeRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    private final QPostLike postLike = QPostLike.postLike;

    public PostLikeRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<PostLike> getLikes(Long postId) {
        return queryFactory
                .selectFrom(postLike)
                .where(postLike.post.id.eq(postId), postLike.canceledAt.isNull())
                .join(postLike.member).fetchJoin()
                .orderBy(postLike.at.desc())
                .fetch();
    }
}
