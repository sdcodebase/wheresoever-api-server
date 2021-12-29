package wheresoever.quickprotoserver.domain.postlike.api;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import wheresoever.quickprotoserver.domain.member.domain.Member;
import wheresoever.quickprotoserver.domain.post.domain.Post;
import wheresoever.quickprotoserver.domain.postlike.application.PostLikeService;
import wheresoever.quickprotoserver.domain.postlike.domain.PostLike;
import wheresoever.quickprotoserver.domain.postlike.exception.AlreadyLikedPostException;
import wheresoever.quickprotoserver.global.constant.HeaderConst;
import wheresoever.quickprotoserver.global.constant.SessionConst;
import wheresoever.quickprotoserver.global.error.exception.ErrorInfo;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = PostLikeApiController.class)
class PostLikeApiControllerTest {

    @MockBean
    private PostLikeService postLikeService;

    private MockMvc mvc;

    @Autowired
    ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private WebApplicationContext ctx;

    @BeforeEach
    private void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(ctx)
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();
    }

    @Test
    void 게시글_좋아요_이미_한_경우() throws Exception {
        given(postLikeService.like(anyLong(), anyLong()))
                .willThrow(new AlreadyLikedPostException());

        MockHttpSession session = new MockHttpSession();
        session.setAttribute(SessionConst.SESSION_MEMBER_ID, 4L);

        mvc.perform(
                MockMvcRequestBuilders.post("/api/posts/1/like")
                        .session(session)
                        .header(HeaderConst.authHeader, "TEST_TOKEN")
        ).andExpect(jsonPath("$.code").value(ErrorInfo.PREV_POST_LIKED.getCode()));
    }

    @Test
    void 게시글_좋아요() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute(SessionConst.SESSION_MEMBER_ID, 4L);

        given(postLikeService.like(anyLong(), anyLong()))
                .willReturn(1L); // 리턴되는 값은 any로 넣을 수 없다.

        mvc.perform(
                MockMvcRequestBuilders.post("/api/posts/1/like")
                        .session(session)
                        .header(HeaderConst.authHeader, "TEST_TOKEN")
        ).andExpect(status().isOk());
    }

    @Test
    void 게시글_좋아요_목록() throws Exception {

        Post post = Post.builder()
                .id(1L)
                .likes(new ArrayList<>())
                .build();

        Member member = Member.builder()
                .id(2L)
                .nickname("sdk")
                .build();

        given(postLikeService.getLikes(anyLong()))
                .willReturn(List.of(new PostLike(post, member)));

        mvc.perform(
                MockMvcRequestBuilders.get("/api/posts/1/like")
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.likes.length()").value(1))
                .andExpect(jsonPath("$.likes[0].memberId").value(2))
                .andExpect(jsonPath("$.likes[0].nickname").value("sdk"));
    }

    @Test
    void 게시글_좋아요_목록없는_경우() throws Exception {
        given(postLikeService.getLikes(anyLong()))
                .willReturn(List.of());

        mvc.perform(
                MockMvcRequestBuilders.get("/api/posts/1/like")
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.likes").isArray())
                .andExpect(jsonPath("$.likes[0]").doesNotExist());
    }
}