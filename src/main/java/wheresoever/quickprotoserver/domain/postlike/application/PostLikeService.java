package wheresoever.quickprotoserver.domain.postlike.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wheresoever.quickprotoserver.domain.member.domain.Member;
import wheresoever.quickprotoserver.domain.post.domain.Post;
import wheresoever.quickprotoserver.domain.postlike.domain.PostLike;
import wheresoever.quickprotoserver.domain.member.dao.MemberRepository;
import wheresoever.quickprotoserver.domain.post.dao.PostRepository;
import wheresoever.quickprotoserver.domain.postlike.dao.PostLikeRepository;
import wheresoever.quickprotoserver.domain.postlike.exception.AlreadyLikedPostException;
import wheresoever.quickprotoserver.global.error.exception.EntityNotFoundException;
import wheresoever.quickprotoserver.global.error.exception.InvalidValueException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    @Transactional
    public Long like(Long memberId, Long postId) {
        List<PostLike> likes = postLikeRepository.getLikes(postId);

        PostLike postLike = likes.stream()
                .filter(like -> like.getMember().getId().equals(memberId))
                .findAny()
                .orElse(null);

        if (Objects.nonNull(postLike)) {
            throw new AlreadyLikedPostException();
        }
        Post post = postRepository.findById(postId).get();
        Member member = memberRepository.findById(memberId).get();

        return postLikeRepository.save(new PostLike(post, member)).getId();
    }

    public List<PostLike> getPostLikes(Long postId) {
        return postLikeRepository.getLikes(postId);
    }

    @Transactional
    public void unlike(Long memberId, Long postId) {
        Optional<PostLike> optional = postLikeRepository.findByMemberIdAndPostId(memberId, postId);

        if (optional.isEmpty()) {
            throw new EntityNotFoundException("????????? ??? ??? ????????????.");
        }

        PostLike like = optional.get();
        if (Objects.nonNull(like.getCanceledAt())) {
            throw new InvalidValueException("?????? ???????????? ??????????????????");
        }

        like.unlike();
    }

    public List<PostLike> getLikes(Long postId) {
        return postLikeRepository.getLikes(postId);
    }

    public Integer getLikeCountOfPost(Long postId) {
        return postLikeRepository.countByPostIdAndCanceledAtIsNotNull(postId);
    }
}
