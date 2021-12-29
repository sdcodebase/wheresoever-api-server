package wheresoever.quickprotoserver.domain.comment.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import wheresoever.quickprotoserver.domain.comment.domain.Comment;
import wheresoever.quickprotoserver.domain.member.dao.MemberRepository;
import wheresoever.quickprotoserver.domain.member.domain.Member;
import wheresoever.quickprotoserver.domain.model.Category;
import wheresoever.quickprotoserver.domain.post.dao.PostRepository;
import wheresoever.quickprotoserver.domain.post.domain.Post;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private EntityManagerFactory emf;

    @Test
    void getCommentsByPostId() {
        //given
        Member member = generateEmptyMember();

        Member commentMember1 = generateEmptyMember();
        Member commentMember2 = generateEmptyMember();

        memberRepository.saveAll(List.of(member, commentMember1, commentMember2));

        Post post = new Post(member, "글의 본문", Category.ART);
        postRepository.save(post);

        Comment c1 = new Comment(commentMember1, post, "댓글1");
        Comment c2 = new Comment(commentMember1, post, "삭제 댓글2");
        c2.delete();

        Comment c3 = new Comment(commentMember2, post, "댓글2");

        commentRepository.saveAll(List.of(c1, c2, c3));

        em.flush();
        em.clear();

        //when

        //then
        List<Comment> commets = commentRepository.getCommentsByPostId(post.getId());
        Comment first = commets.get(0);
        Comment second = commets.get(1);

        assertThat(first.getAt()).isBefore(second.getAt());
        assertThat(
                commets.stream()
                        .anyMatch(comment -> comment.getId().equals(c2.getId()))
        ).isFalse();

        assertThat(emf.getPersistenceUnitUtil().isLoaded(first.getMember())).isTrue();


    }

    private Member generateEmptyMember() {
        return Member.builder().build();
    }
}