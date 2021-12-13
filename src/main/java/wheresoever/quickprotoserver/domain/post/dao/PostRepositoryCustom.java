package wheresoever.quickprotoserver.domain.post.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import wheresoever.quickprotoserver.domain.post.domain.Post;

public interface PostRepositoryCustom {

    Post getPostDetail(Long postId);

    Page<Post> getPostList(Pageable pageable, Long memberId);
}
