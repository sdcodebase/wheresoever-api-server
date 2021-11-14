package wheresoever.quickprotoserver.repository.postlike;

import wheresoever.quickprotoserver.domain.PostLike;

import java.util.List;

public interface PostLikeRepositoryCustom {
    List<PostLike> getLikes(Long postId);
}
