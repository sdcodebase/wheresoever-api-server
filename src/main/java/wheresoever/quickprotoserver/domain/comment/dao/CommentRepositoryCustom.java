package wheresoever.quickprotoserver.domain.comment.dao;

import wheresoever.quickprotoserver.domain.comment.domain.Comment;

import java.util.List;

public interface CommentRepositoryCustom {

    List<Comment> getCommentsByPostId(Long postId);
}
