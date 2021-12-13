package wheresoever.quickprotoserver.domain.comment.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wheresoever.quickprotoserver.domain.comment.domain.Comment;
import wheresoever.quickprotoserver.domain.member.domain.Member;
import wheresoever.quickprotoserver.domain.post.domain.Post;
import wheresoever.quickprotoserver.domain.comment.dao.CommentRepository;
import wheresoever.quickprotoserver.domain.member.dao.MemberRepository;
import wheresoever.quickprotoserver.domain.post.dao.PostRepository;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    @Transactional
    public Long comment(Long memberId, Long postId, String content) {
        Member member = memberRepository.findById(memberId).get();
        Post post = postRepository.findById(postId).get();

        Comment comment = new Comment(member, post, content);
        commentRepository.save(comment);

        return comment.getId();
    }

    @Transactional
    public Long recomment(Long commentId, String content) {
        Comment comment = commentRepository.findById(commentId).get();
        comment.recomment(content);

        return comment.getId();
    }

    @Transactional
    public void delete(Long commentId) {
        Comment comment = commentRepository.findById(commentId).get(); // 추후 예외처리
        comment.delete();
    }

}
