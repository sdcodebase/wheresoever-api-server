package wheresoever.quickprotoserver.domain.comment.dao;

import com.querydsl.jpa.impl.JPAQueryFactory;
import wheresoever.quickprotoserver.domain.comment.domain.Comment;
import wheresoever.quickprotoserver.domain.comment.domain.QComment;

import javax.persistence.EntityManager;
import java.util.List;

public class CommentRepositoryCustomImpl implements CommentRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public CommentRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    private final QComment comment = QComment.comment;

    @Override
    public List<Comment> getCommentsByPostId(Long postId) {
        return queryFactory
                .selectFrom(comment)
                .where(
                        comment.post.id.eq(postId),
                        comment.canceledAt.isNull()
                ).join(comment.member).fetchJoin()
                .orderBy(comment.at.asc())
                .fetch();
    }
}