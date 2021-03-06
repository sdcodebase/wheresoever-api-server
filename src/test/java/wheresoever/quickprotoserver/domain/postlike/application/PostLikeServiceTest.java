package wheresoever.quickprotoserver.domain.postlike.application;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import wheresoever.quickprotoserver.domain.member.dao.MemberRepository;
import wheresoever.quickprotoserver.domain.member.domain.Member;
import wheresoever.quickprotoserver.domain.post.dao.PostRepository;
import wheresoever.quickprotoserver.domain.post.domain.Post;
import wheresoever.quickprotoserver.domain.postlike.dao.PostLikeRepository;
import wheresoever.quickprotoserver.domain.postlike.domain.PostLike;
import wheresoever.quickprotoserver.domain.postlike.exception.AlreadyLikedPostException;
import wheresoever.quickprotoserver.global.error.exception.EntityNotFoundException;
import wheresoever.quickprotoserver.global.error.exception.InvalidValueException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
class PostLikeServiceTest {

    @Mock
    PostLikeRepository postLikeRepository;

    @Mock
    PostRepository postRepository;

    @Mock
    MemberRepository memberRepository;

    @InjectMocks
    PostLikeService postLikeService;

    @Test
    void κΈ_μ’μμ() {
        //given
        Member liker = Member.builder()
                .id(1L)
                .build();

        Post post = Post.builder()
                .likes(new ArrayList<>())
                .id(2L)
                .build();

        given(postLikeRepository.getLikes(anyLong()))
                .willReturn(List.of());

        given(postRepository.findById(anyLong()))
                .willReturn(Optional.of(post));
        given(memberRepository.findById(anyLong()))
                .willReturn(Optional.of(liker));

        PostLike postLike = PostLike.builder()
                .id(1L)
                .member(liker)
                .post(post)
                .build();

        given(postLikeRepository.save(any()))
                .willReturn(postLike);

        //when
        Long postLikeId = postLikeService.like(liker.getId(), post.getId());

        //then
        assertThat(postLikeId).isEqualTo(postLike.getId());
    }

    @Test
    void κΈ_μ’μμ_μ€λ³΅() {
        //given
        Member liker = Member.builder()
                .id(1L)
                .build();

        PostLike postLike = PostLike.builder()
                .id(2L)
                .member(liker)
                .build();

        given(postLikeRepository.getLikes(anyLong()))
                .willReturn(List.of(postLike));

        assertThatThrownBy(() -> {
            postLikeService.like(liker.getId(), anyLong());
        }).isInstanceOf(AlreadyLikedPostException.class);

    }

    @Test
    void κΈ_μ’μμ_μ·¨μ() {
        //given
        PostLike postLike = PostLike.builder()
                .id(1L)
                .build();

        given(postLikeRepository.findByMemberIdAndPostId(anyLong(), anyLong()))
                .willReturn(Optional.of(postLike));

        //when
        postLikeService.unlike(anyLong(), anyLong());

        //then
        assertThat(postLike.getCanceledAt()).isNotNull();
    }

    @Test
    void κΈ_μ’μμ_μ·¨μ_μ€λ³΅() {
        //given
        PostLike postLike = PostLike.builder()
                .id(1L)
                .canceledAt(LocalDateTime.now())
                .build();

        given(postLikeRepository.findByMemberIdAndPostId(anyLong(), anyLong()))
                .willReturn(Optional.of(postLike));

        //then
        assertThatThrownBy(() -> {
            //when
            postLikeService.unlike(anyLong(), anyLong());
        }).isInstanceOf(InvalidValueException.class);
    }

    @Test
    void κΈ_μ’μμ_μνλλ°_μ’μμ_μ·¨μ() {
        //given
        given(postLikeRepository.findByMemberIdAndPostId(anyLong(), anyLong()))
                .willReturn(Optional.empty());

        //then
        assertThatThrownBy(() -> {
            //when
            postLikeService.unlike(anyLong(), anyLong());
        }).isInstanceOf(EntityNotFoundException.class);
    }
}