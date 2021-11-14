package wheresoever.quickprotoserver.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wheresoever.quickprotoserver.domain.Comment;
import wheresoever.quickprotoserver.domain.CommentChild;
import wheresoever.quickprotoserver.domain.Member;
import wheresoever.quickprotoserver.repository.comment.CommentRepository;
import wheresoever.quickprotoserver.repository.commentchild.CommentChildRepository;
import wheresoever.quickprotoserver.repository.member.MemberRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentChildService {

    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;
    private final CommentChildRepository commentChildRepository;

    @Transactional
    public Long comment(Long memberId, Long commentId, String content) {
        Member member = memberRepository.findById(memberId).get();
        Comment comment = commentRepository.findById(commentId).get();

        CommentChild commentChild = new CommentChild(member, comment, content);
        commentChildRepository.save(commentChild);

        return commentChild.getId();
    }

    @Transactional
    public Long recomment(Long commentChildId, String content) {
        CommentChild commentChild = commentChildRepository.findById(commentChildId).get();
        commentChild.recomment(content);

        return commentChild.getId();
    }

    @Transactional
    public void delete(Long commentChildId) {
        CommentChild commentChild = commentChildRepository.findById(commentChildId).get();
        commentChild.delete();
    }
}
