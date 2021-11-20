package wheresoever.quickprotoserver.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import wheresoever.quickprotoserver.domain.Category;
import wheresoever.quickprotoserver.domain.Member;
import wheresoever.quickprotoserver.domain.Sex;
import wheresoever.quickprotoserver.repository.commentchild.CommentChildRepository;

import java.time.LocalDate;

@SpringBootTest
@Transactional
class CommentChildServiceTest {

    @Autowired
    private CommentChildService commentChildService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private PostService postService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentChildRepository commentChildRepository;

    @Test
    public void 대댓글_작성() throws Exception {
        //given
        Member postMember = new Member("aaa@gmail", "213", Sex.MALE, "헤헤", LocalDate.now(), "서울");
        Long postMemberId = memberService.join(postMember);

        Member commentMember = new Member("bbb@gmail", "213", Sex.MALE, "헤헤", LocalDate.now(), "서울");
        Long commentMemberId = memberService.join(commentMember);

        Member childMember = new Member("ccc@gmail", "213", Sex.MALE, "헤헤", LocalDate.now(), "서울");
        Long childMemberId = memberService.join(childMember);

        Long postId = postService.posts(postMemberId, "글입니다.", Category.ART);
        Long commentId = commentService.comment(commentMemberId, postId, "댓글입니다.");

        //when
        Long childId = commentChildService.comment(childMemberId, commentId, "대댓글입니다.");

        //then
        Assertions.assertThat(childId).isEqualTo(commentChildRepository.findById(childId).get().getId());
    }

}