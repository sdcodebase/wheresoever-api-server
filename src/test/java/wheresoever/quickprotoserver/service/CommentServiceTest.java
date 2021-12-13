package wheresoever.quickprotoserver.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import wheresoever.quickprotoserver.domain.comment.application.CommentService;
import wheresoever.quickprotoserver.domain.member.application.MemberService;
import wheresoever.quickprotoserver.domain.post.application.PostService;
import wheresoever.quickprotoserver.domain.model.Category;
import wheresoever.quickprotoserver.domain.comment.domain.Comment;
import wheresoever.quickprotoserver.domain.member.domain.Member;
import wheresoever.quickprotoserver.domain.model.Sex;
import wheresoever.quickprotoserver.domain.comment.dao.CommentRepository;

import java.time.LocalDate;

@SpringBootTest
@Transactional
class CommentServiceTest {

    @Autowired
    private CommentService commentService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private PostService postService;

    @Autowired
    private CommentRepository commentRepository;

    @Test
    public void 댓글_쓰기() throws Exception {
        //given
        Member member = new Member("aaa@gmail", "213", Sex.MALE, "헤헤", LocalDate.now(), "서울");
        Long memberId = memberService.join(member);

        String content = "처음으로 쓰는 글입니다.";
        Long postId = postService.posts(memberId, content, Category.ART);

        //when
        String commentContent = "첫 댓글입니다.";
        Long commentId = commentService.comment(memberId, postId, commentContent);

        //then
        Comment comment = commentRepository.findById(commentId).get();
        Assertions.assertThat(commentId).isEqualTo(comment.getId());
    }
}