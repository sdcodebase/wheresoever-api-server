package wheresoever.quickprotoserver.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import wheresoever.quickprotoserver.domain.*;
import wheresoever.quickprotoserver.repository.post.PostRepository;
import wheresoever.quickprotoserver.repository.postlike.PostLikeRepository;

import java.time.LocalDate;


@SpringBootTest
@Transactional
class PostLikeServiceTest {

    @Autowired
    private PostLikeService postLikeService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostLikeRepository postLikeRepository;

    @Test
    public void 글_좋아요() throws Exception {
        //given
        Member member = new Member("sdkim123@gmail.com", "1234", Sex.MALE, "sundo", LocalDate.now(), "서울");
        Long memberId = memberService.join(member);

        Long postId = postService.posts(memberId, "게시글22", Category.ART);

        PostLike postLike = new PostLike(postRepository.findById(postId).get(), member);

        //when
        Long likeId = postLikeService.like(memberId, postId);

        //then
        PostLike findPostLike = postLikeRepository.findById(likeId).get();

        Assertions.assertThat(findPostLike.getMember().getId()).isEqualTo(postLike.getMember().getId());
        Assertions.assertThat(findPostLike.getPost().getId()).isEqualTo(postLike.getPost().getId());
    }

    @Test
    public void 글_좋아요_중복() throws Exception {
        //given

        Member member = new Member("sdkim123@gmail.com", "1234", Sex.MALE, "sundo", LocalDate.now(), "서울");
        Long memberId = memberService.join(member);

        Long postId = postService.posts(memberId, "게시글22", Category.ART);

        postLikeService.like(memberId, postId);

        //then
        Assertions.assertThatThrownBy(() -> {
            //when
            postLikeService.like(memberId, postId);
        }).isInstanceOf(IllegalStateException.class);
    }

    @Test
    public void 글_좋아요_취소() throws Exception {
        //given
        Member member = new Member("sdkim123@gmail.com", "1234", Sex.MALE, "sundo", LocalDate.now(), "서울");
        Long memberId = memberService.join(member);

        Long postId = postService.posts(memberId, "게시글22", Category.ART);

        Long likeId = postLikeService.like(memberId, postId);

        //when
        postLikeService.unlike(memberId, postId);

        //then
        PostLike like = postLikeRepository.findById(likeId).get();
        Assertions.assertThat(like.getCanceledAt()).isNotNull();
    }

    @Test
    public void 글_좋아요_취소_중복() throws Exception {
        //given
        Member member = new Member("sdkim123@gmail.com", "1234", Sex.MALE, "sundo", LocalDate.now(), "서울");
        Long memberId = memberService.join(member);

        Long postId = postService.posts(memberId, "게시글22", Category.ART);

        Long likeId = postLikeService.like(memberId, postId);

        postLikeService.unlike(memberId, postId);

        //then
        Assertions.assertThatThrownBy(() -> {
            //when
            postLikeService.unlike(memberId, postId);
        }).isInstanceOf(IllegalStateException.class);
    }

    @Test
    public void 글_좋아요_안했는데_취소하기() throws Exception {
        //given
        Member member = new Member("sdkim123@gmail.com", "1234", Sex.MALE, "sundo", LocalDate.now(), "서울");
        Long memberId = memberService.join(member);

        Long postId = postService.posts(memberId, "게시글22", Category.ART);

        //then
        Assertions.assertThatThrownBy(() -> {
            //when
            postLikeService.unlike(0L, postId);
        }).isInstanceOf(IllegalStateException.class);
    }
}