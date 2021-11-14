package wheresoever.quickprotoserver.repository.post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wheresoever.quickprotoserver.domain.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {
}
