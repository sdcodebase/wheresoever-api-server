package wheresoever.quickprotoserver.domain.post.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wheresoever.quickprotoserver.domain.model.Category;
import wheresoever.quickprotoserver.domain.member.domain.Member;
import wheresoever.quickprotoserver.domain.post.domain.Post;
import wheresoever.quickprotoserver.domain.post.dto.PostDetailDto;
import wheresoever.quickprotoserver.domain.member.dao.MemberRepository;
import wheresoever.quickprotoserver.domain.post.dao.PostRepository;
import wheresoever.quickprotoserver.domain.postlike.dao.PostLikeRepository;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final PostLikeRepository postLikeRepository;

    public Page<Post> getPostPage(Pageable pageable, Long memberId) {
        return postRepository.getPostList(pageable, memberId);
    }


    @Transactional
    public Long posts(Long memberId, String content, Category category) {
        Optional<Member> optionalMember = memberRepository.findById(memberId);
        if (optionalMember.isEmpty()) {
            throw new IllegalStateException("does not exist member");
        }

        Member member = optionalMember.get();

        Post post = new Post(member, content, category);
        postRepository.save(post);

        return post.getId();
    }

    @Transactional
    public Boolean delete(Long postId) {
        Optional<Post> optionalPost = postRepository.findById(postId);
        if (optionalPost.isEmpty()) {
            throw new IllegalStateException("does not exist post");
        }

        Post post = optionalPost.get();
        if (post.getCanceledAt() != null) {
            return false;
        }

        post.delete();
        return true;
    }

    public PostDetailDto getPostDetailInfo(Long postId) {
        Post postDetail = postRepository.getPostDetail(postId);

        int likeCount = postLikeRepository.countByPost(postDetail);

        return new PostDetailDto(postDetail, likeCount);
    }
}