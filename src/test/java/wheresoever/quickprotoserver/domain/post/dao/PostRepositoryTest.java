package wheresoever.quickprotoserver.domain.post.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import wheresoever.quickprotoserver.domain.member.dao.MemberRepository;
import wheresoever.quickprotoserver.domain.member.domain.Member;
import wheresoever.quickprotoserver.domain.model.Category;
import wheresoever.quickprotoserver.domain.post.domain.Post;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    EntityManager em;

    @Autowired
    EntityManagerFactory emf;

    @Test
    void getPostList() {
        //given
        Member member = Member.builder().build();
        memberRepository.save(member);

        Post p1 = new Post(member, "게시글1", Category.ART);
        Post p2 = new Post(member, "게시글2", Category.ART);
        Post p3 = new Post(member, "게시글3", Category.ART);
        Post p4 = new Post(member, "게시글4", Category.ART);
        postRepository.saveAll(List.of(p1, p2, p3, p4));

        em.flush();
        em.clear();

        Pageable pageable = PageRequest.of(0, 2);

        //when
        Page<Post> postPage = postRepository.getPostList(pageable, member.getId());

        //then
        List<Post> posts = postPage.getContent();
        Post first = posts.get(0);
        Post second = posts.get(1);

        assertThat(p4.getId()).isEqualTo(first.getId());
        assertThat(p3.getId()).isEqualTo(second.getId());
        assertThat(postPage.getTotalElements()).isEqualTo(4L);
        assertThat(postPage.getSize()).isEqualTo(2);
        assertThat(postPage.hasNext()).isTrue();

        assertThat(emf.getPersistenceUnitUtil().isLoaded(first.getMember())).isTrue();
    }

}