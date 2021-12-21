package wheresoever.quickprotoserver.domain.post.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wheresoever.quickprotoserver.domain.member.dao.MemberRepository;
import wheresoever.quickprotoserver.domain.member.domain.Member;
import wheresoever.quickprotoserver.domain.member.exception.MemberNotFoundException;
import wheresoever.quickprotoserver.domain.model.Category;
import wheresoever.quickprotoserver.domain.post.dao.PostRepository;
import wheresoever.quickprotoserver.domain.post.domain.Post;
import wheresoever.quickprotoserver.domain.post.exception.PostNotFoundException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    public Page<Post> getPostPage(Pageable pageable, Long memberId) {
        return postRepository.getPostList(pageable, memberId);
    }

    @Transactional
    public Long posts(Long memberId, String content, Category category) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);

        Post post = new Post(member, content, category);

        postRepository.save(post);

        return post.getId();
    }

    @Transactional
    public Boolean delete(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);

        if (post.getCanceledAt() != null) {
            return false;
        }

        post.delete();
        return true;
    }

    public Post getPostInfo(Long postId) {
        return postRepository.findByIdAndCanceledAtIsNull(postId)
                .orElseThrow(PostNotFoundException::new);
    }
}