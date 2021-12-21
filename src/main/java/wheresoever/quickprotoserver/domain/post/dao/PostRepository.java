package wheresoever.quickprotoserver.domain.post.dao;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wheresoever.quickprotoserver.domain.post.domain.Post;

import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {

    @EntityGraph(attributePaths = {"member"})
    Optional<Post> findByIdAndCanceledAtIsNull(Long id);

}
