package wheresoever.quickprotoserver.domain.comment.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wheresoever.quickprotoserver.domain.comment.dao.CommentRepository;
import wheresoever.quickprotoserver.domain.comment.domain.Comment;
import wheresoever.quickprotoserver.domain.comment.exception.CommentNotFoundException;
import wheresoever.quickprotoserver.domain.member.dao.MemberRepository;
import wheresoever.quickprotoserver.domain.member.domain.Member;
import wheresoever.quickprotoserver.domain.member.exception.MemberNotFoundException;
import wheresoever.quickprotoserver.domain.post.dao.PostRepository;
import wheresoever.quickprotoserver.domain.post.domain.Post;
import wheresoever.quickprotoserver.domain.post.exception.PostNotFoundException;

import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    @Transactional
    public Long comment(Long memberId, Long postId, String content) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);

        Post post = postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);

        return commentRepository.save(new Comment(member, post, content)).getId();
    }

    @Transactional
    public void recomment(Long commentId, String content) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
        comment.recomment(content);
    }

    @Transactional
    public void delete(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
        comment.delete();
    }

    public List<Comment> getCommentsOfPost(Long postId) {
        return commentRepository.getCommentsByPostId(postId);
    }

}
