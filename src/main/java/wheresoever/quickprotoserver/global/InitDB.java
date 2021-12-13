package wheresoever.quickprotoserver.global;

import lombok.RequiredArgsConstructor;
import wheresoever.quickprotoserver.domain.model.Category;
import wheresoever.quickprotoserver.domain.member.domain.Member;
import wheresoever.quickprotoserver.domain.model.Sex;
import wheresoever.quickprotoserver.domain.commentchild.application.CommentChildService;
import wheresoever.quickprotoserver.domain.comment.application.CommentService;
import wheresoever.quickprotoserver.domain.member.application.MemberService;
import wheresoever.quickprotoserver.domain.post.application.PostService;

import javax.annotation.PostConstruct;
import java.time.LocalDate;

//@Component
@RequiredArgsConstructor
public class InitDB {
    private final MemberService memberService;
    private final PostService postService;
    private final CommentService commentService;
    private final CommentChildService commentChildService;

    @PostConstruct
    public void init() {
        Member postMember = new Member("aaa@gmail", "213", Sex.MALE, "헤헤", LocalDate.now(), "서울");
        Long postMemberId = memberService.join(postMember);

        Member commentMember = new Member("bbb@gmail", "213", Sex.MALE, "헤헤", LocalDate.now(), "서울");
        Long commentMemberId = memberService.join(commentMember);

        Member childMember1 = new Member("ccc@gmail", "213", Sex.MALE, "헤헤", LocalDate.now(), "서울");

        Long childMember1Id = memberService.join(childMember1);

        Member childMember2 = new Member("ddd@gmail", "213", Sex.MALE, "헤헤", LocalDate.now(), "서울");
        Long childMember2Id = memberService.join(childMember2);

        Long postId = postService.posts(postMemberId, "게시글", Category.ART);

        Long commentId1 = commentService.comment(commentMemberId, postId, "1. 댓글작성자의 댓글");
        commentService.comment(commentMemberId, postId, "2. 댓글작성자의 댓글");
        Long commentId2 = commentService.comment(postMemberId, postId, "3. 게시글작성자의 댓글");

        commentChildService.comment(childMember1Id, commentId1, "commentId1의 대댓글1");
        commentChildService.comment(childMember1Id, commentId2, "commentId2의 대댓글2");
        commentChildService.comment(childMember2Id, commentId2, "commentId2의 대댓글3");

    }

}
