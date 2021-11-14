package wheresoever.quickprotoserver.repository.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wheresoever.quickprotoserver.domain.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
}
