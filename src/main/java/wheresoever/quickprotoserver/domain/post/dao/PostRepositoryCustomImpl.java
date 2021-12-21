package wheresoever.quickprotoserver.domain.post.dao;


import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import wheresoever.quickprotoserver.domain.post.domain.Post;
import wheresoever.quickprotoserver.domain.post.domain.QPost;

import javax.persistence.EntityManager;
import java.util.List;


public class PostRepositoryCustomImpl implements PostRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    private final QPost post = QPost.post;

    public PostRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<Post> getPostList(Pageable pageable, Long memberId) {
        List<Post> posts = queryFactory
                .selectFrom(post)
                .where(memberEq(memberId))
                .join(post.member).fetchJoin()
                .orderBy(post.at.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Post> countQuery = queryFactory
                .selectFrom(post)
                .where(memberEq(memberId));

        return PageableExecutionUtils.getPage(posts, pageable, countQuery::fetchCount);
    }

    private BooleanExpression memberEq(Long memberCond) {
        return memberCond != null ? post.member.id.eq(memberCond) : null;
    }
}
