package wheresoever.quickprotoserver.domain.postlike.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wheresoever.quickprotoserver.domain.postlike.domain.PostLike;

import java.util.Optional;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, Long>, PostLikeRepositoryCustom {
    int countByPostIdAndCanceledAtIsNotNull(Long postId);

    Optional<PostLike> findByMemberIdAndPostId(Long memberId, Long postId);
}
