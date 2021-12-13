package wheresoever.quickprotoserver.domain.commentchild.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wheresoever.quickprotoserver.domain.commentchild.domain.CommentChild;

@Repository
public interface CommentChildRepository extends JpaRepository<CommentChild, Long> {
}
