package wheresoever.quickprotoserver.domain.postlike.dao;

import wheresoever.quickprotoserver.domain.postlike.domain.PostLike;

import java.util.List;

public interface PostLikeRepositoryCustom {
    List<PostLike> getLikes(Long postId);
}
