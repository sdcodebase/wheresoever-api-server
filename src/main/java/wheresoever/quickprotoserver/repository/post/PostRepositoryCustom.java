package wheresoever.quickprotoserver.repository.post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import wheresoever.quickprotoserver.domain.Post;

import java.util.List;

public interface PostRepositoryCustom {

    Post getPostDetail(Long postId);

    Page<Post> getPostList(Pageable pageable, Long memberId);
}
