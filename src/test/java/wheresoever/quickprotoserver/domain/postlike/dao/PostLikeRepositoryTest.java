package wheresoever.quickprotoserver.domain.postlike.dao;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import wheresoever.quickprotoserver.domain.member.dao.MemberRepository;
import wheresoever.quickprotoserver.domain.member.domain.Member;
import wheresoever.quickprotoserver.domain.model.Category;
import wheresoever.quickprotoserver.domain.post.dao.PostRepository;
import wheresoever.quickprotoserver.domain.post.domain.Post;
import wheresoever.quickprotoserver.domain.postlike.domain.PostLike;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class PostLikeRepositoryTest {

    @Autowired
    EntityManagerFactory emf;

    @Autowired
    EntityManager em;

    @Autowired
    private PostLikeRepository postLikeRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private MemberRepository memberRepository;


    private Member generateEmptyMember() {
        return Member.builder().build();
    }

    @Test
    void getLikes() {
        //given
        Member postWriter = generateEmptyMember();
        Member member1 = generateEmptyMember();
        Member member2 = generateEmptyMember();
        Member member3 = generateEmptyMember();
        memberRepository.saveAll(List.of(postWriter, member1, member2, member3));

        Post post = new Post(postWriter, "안녕", Category.ART);
        postRepository.save(post);

        PostLike postLike1 = new PostLike(post, member1);
        PostLike postLike2 = new PostLike(post, member2);
        PostLike postLike3 = new PostLike(post, member3);
        postLike3.unlike();

        postLikeRepository.saveAll(List.of(postLike1, postLike2, postLike3));

        em.flush();
        em.clear();

        //when
        List<PostLike> likes = postLikeRepository.getLikes(post.getId());

        //then
        PostLike first = likes.get(0);
        PostLike second = likes.get(1);

        // 좋아요 오름차순 정렬
        assertThat(first.getAt()).isAfter(second.getAt());

        // 삭제된 좋아요는 리스팅X
        Long member3Id = postLike3.getMember().getId();
        boolean postLike3Existence = likes.stream().anyMatch(like -> like.getMember().getId().equals(member3Id));
        assertThat(postLike3Existence).isFalse();

        // PostLike.member 패치조인
        boolean isMemberFetched = emf.getPersistenceUnitUtil().isLoaded(first.getMember());
        assertThat(isMemberFetched).isTrue();
    }
}