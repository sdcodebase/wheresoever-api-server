package wheresoever.quickprotoserver.repository.commentchild;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wheresoever.quickprotoserver.domain.CommentChild;

@Repository
public interface CommentChildRepository extends JpaRepository<CommentChild, Long> {
}
