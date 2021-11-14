package wheresoever.quickprotoserver.repository.postlike;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wheresoever.quickprotoserver.domain.Post;
import wheresoever.quickprotoserver.domain.PostLike;

import java.util.Optional;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, Long>, PostLikeRepositoryCustom {
    int countByPost(Post post);

    Optional<PostLike> findByMemberIdAndPostId(Long postId, Long memberId);
}
