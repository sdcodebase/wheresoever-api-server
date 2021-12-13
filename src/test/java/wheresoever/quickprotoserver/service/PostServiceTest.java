package wheresoever.quickprotoserver.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import wheresoever.quickprotoserver.domain.comment.application.CommentService;
import wheresoever.quickprotoserver.domain.comment.domain.Comment;
import wheresoever.quickprotoserver.domain.commentchild.application.CommentChildService;
import wheresoever.quickprotoserver.domain.commentchild.domain.CommentChild;
import wheresoever.quickprotoserver.domain.member.application.MemberService;
import wheresoever.quickprotoserver.domain.post.application.PostService;
import wheresoever.quickprotoserver.domain.post.domain.Post;
import wheresoever.quickprotoserver.domain.model.Sex;
import wheresoever.quickprotoserver.domain.member.domain.Member;
import wheresoever.quickprotoserver.domain.model.Category;
import wheresoever.quickprotoserver.domain.post.dao.PostRepository;
import wheresoever.quickprotoserver.domain.postlike.dao.PostLikeRepository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.time.LocalDate;
import java.util.List;

@SpringBootTest
@Transactional
class PostServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentChildService commentChildService;

    @Autowired
    private PostLikeRepository postLikeRepository;

    @Autowired
    EntityManager em;

    @Autowired
    EntityManagerFactory emf;

    @Test
    public void 게시글_쓰기() throws Exception {
        //given
        Member member = new Member("sdkim@gmail.com", "1234", Sex.MALE, "sundo", LocalDate.now(), "서울");
        Long memberId = memberService.join(member);

        //when
        String content = "처음으로 쓰는 글입니다.";
        Long postId = postService.posts(memberId, content, Category.ART);

        //then
        Assertions.assertThat(postId).isEqualTo(postRepository.findById(postId).get().getId());
    }


    @Test
//    @Rollback(false)
    public void 게시글_상세_조회() throws Exception {
        //given
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
        Long commentId2 = commentService.comment(commentMemberId, postId, "2. 댓글작성자의 댓글");
        Long commentId3 = commentService.comment(postMemberId, postId, "3. 게시글작성자의 댓글");

        Long chId1 = commentChildService.comment(childMember1Id, commentId1, "commentId1의 대댓글1");
        Long chId2 = commentChildService.comment(childMember1Id, commentId3, "commentId2의 대댓글2");
        Long chId3 = commentChildService.comment(childMember2Id, commentId3, "commentId2의 대댓글3");

        em.flush();
        em.clear();

        //when
        Post postDetail = postRepository.getPostDetail(postId);

        //then
        Assertions.assertThat(emf.getPersistenceUnitUtil().isLoaded(postDetail.getMember())).isEqualTo(true);

        /* 시간순 정렬 확인 */
        List<Comment> comments = postDetail.getComments();
        for (int i = 1; i < comments.size(); i++) {
            Comment comment = comments.get(i);
            Assertions.assertThat(comment.getAt()).isBefore(comments.get(i - 1).getAt());

            List<CommentChild> commentChildren = comment.getCommentChildren();
            for (int j = 1; j < commentChildren.size(); j++) {
                CommentChild commentChild = commentChildren.get(j);
                Assertions.assertThat(commentChild.getAt()).isBefore(commentChildren.get(j - 1).getAt());
            }
        }
    }
}