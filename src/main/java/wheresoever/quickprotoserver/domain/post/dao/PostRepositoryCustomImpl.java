package wheresoever.quickprotoserver.domain.post.dao;


import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import wheresoever.quickprotoserver.domain.comment.domain.QComment;
import wheresoever.quickprotoserver.domain.post.domain.Post;
import wheresoever.quickprotoserver.domain.post.domain.QPost;

import javax.persistence.EntityManager;
import java.util.List;


public class PostRepositoryCustomImpl implements PostRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    private final QPost post = QPost.post;
    private final QComment comment = QComment.comment;

    public PostRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Post getPostDetail(Long postId) {
        /*
            XToMany 조인 1번하는건 괜찮다.
            하지만, XToMany에서 한 다리 건너(한번더)
             XToMany를 조인하게 되면 (3개 테이블 하나의 로우로)
             로우의 개수가 뻥튀기된다.
             그리고, DB의 메모리도 많이 먹을 수 있어서 그냥 LAZY INIT하는게 낫다.
             XToOne을 하는건 괜찮다.
         */
        Post post = queryFactory
                .selectFrom(this.post)
                .where(this.post.id.eq(postId))
                .join(this.post.comments, comment).fetchJoin()
                .orderBy(comment.at.desc())
                .fetchOne();

        // LAZY INIT
        // find CommentChild

//        // if문 처리하기?
//        post.getComments()
//                .forEach(comment -> comment.getCommentChildren()
//                        .sort((c1, c2) -> c1.getAt().isEqual(c2.getAt()) ? 0 : c1.getAt().isAfter(c2.getAt()) ? 1 : -1));

        // LAZY INIT
        // find Member from Post, Comment, CommentChild

        // if문 처리하기?
//        post.getComments().get(0).getMember().getEmail();

        return post;
    }

    @Override
    public Page<Post> getPostList(Pageable pageable, Long memberId) {
        List<Post> posts = queryFactory
                .selectFrom(post)
                .where(memberEq(memberId))
                .orderBy(post.at.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Post> countQuery = queryFactory
                .selectFrom(post);

        return PageableExecutionUtils.getPage(posts, pageable, countQuery::fetchCount);
    }

    private BooleanExpression memberEq(Long memberCond) {
        return memberCond != null ? post.member.id.eq(memberCond) : null;
    }
}
