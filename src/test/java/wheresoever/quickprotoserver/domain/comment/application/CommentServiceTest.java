package wheresoever.quickprotoserver.domain.comment.application;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import wheresoever.quickprotoserver.domain.comment.dao.CommentRepository;
import wheresoever.quickprotoserver.domain.comment.domain.Comment;
import wheresoever.quickprotoserver.domain.member.dao.MemberRepository;
import wheresoever.quickprotoserver.domain.member.domain.Member;
import wheresoever.quickprotoserver.domain.member.exception.MemberNotFoundException;
import wheresoever.quickprotoserver.domain.model.Category;
import wheresoever.quickprotoserver.domain.post.dao.PostRepository;
import wheresoever.quickprotoserver.domain.post.domain.Post;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.spy;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    CommentRepository commentRepository;

    @Mock
    MemberRepository memberRepository;

    @Mock
    PostRepository postRepository;

    @InjectMocks
    CommentService commentService;

    @Test
    void 댓글_쓰기() {
        //given
        Member member = Member.builder()
                .id(1L)
                .build();

        Post post = Post.builder()
                .id(1L)
                .member(member)
                .content("내용")
                .category(Category.ART)
                .comments(new ArrayList<>())
                .build();

        given(memberRepository.findById(any()))
                .willReturn(Optional.of(member));

        given(postRepository.findById(any()))
                .willReturn(Optional.of(post));

        Comment comment = new Comment(member, post, "댓글");
        given(commentRepository.save(any()))
                .willReturn(comment);

        //when
        Long commentId = commentService.comment(member.getId(), post.getId(), "안녕하세요");

        //then
        assertThat(commentId).isEqualTo(comment.getId());
        assertThat(post.getComments().get(0)).isEqualTo(comment);
    }

    @Test
    void 댓글쓰기_글_없을때() {
        given(memberRepository.findById(anyLong()))
                .willReturn(Optional.of(Member.builder().build()));

        given(postRepository.findById(anyLong()))
                .willThrow(MemberNotFoundException.class);

        assertThatThrownBy(() -> commentService.comment(1L, 1L, "aaa"))
                .isInstanceOf(MemberNotFoundException.class);
    }

    @Test
    void 댓글_삭제() {
        Comment comment = spy(Comment.class);
        comment.delete();
        assertThat(comment.getCanceledAt()).isNotNull();
    }
}