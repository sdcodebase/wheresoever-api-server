package wheresoever.quickprotoserver.domain.post.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import wheresoever.quickprotoserver.domain.member.domain.Member;
import wheresoever.quickprotoserver.domain.post.application.PostService;
import wheresoever.quickprotoserver.domain.post.domain.Post;
import wheresoever.quickprotoserver.domain.post.dto.request.CreatePostRequest;
import wheresoever.quickprotoserver.domain.post.exception.PostNotFoundException;
import wheresoever.quickprotoserver.domain.postlike.application.PostLikeService;
import wheresoever.quickprotoserver.domain.postlike.domain.PostLike;
import wheresoever.quickprotoserver.global.constant.HeaderConst;
import wheresoever.quickprotoserver.global.constant.SessionConst;
import wheresoever.quickprotoserver.global.error.exception.ErrorInfo;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = PostApiController.class)
class PostApiControllerTest {

    @MockBean
    private PostService postService;

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
    void 글_작성하기() throws Exception {
        given(postService.posts(any(), anyString(), any()))
                .willReturn(1L);

        MockHttpSession session = new MockHttpSession();
        session.setAttribute(SessionConst.SESSION_MEMBER_ID, 1L);

        CreatePostRequest request = CreatePostRequest.builder()
                .content("글의 내용")
                .category("예술")
                .build();

        mvc.perform(
                MockMvcRequestBuilders.post("/api/posts")
                        .content(mapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .session(session)
                        .header(HeaderConst.authHeader, "TEST_CODE")
        )
                .andExpect(jsonPath("$.postId").exists());
    }

    @Test
    void 삭제된_글_조회시도() throws Exception {
        given(postService.getPostInfo(anyLong()))
                .willThrow(new PostNotFoundException());

        mvc.perform(
                MockMvcRequestBuilders.get("/api/posts/2")
        )
                .andExpect(jsonPath("$.code").value(ErrorInfo.POST_NOT_FOUND.getCode()));
    }

    @Test
    void 글_조회하기() throws Exception {
        Member member = Member.builder()
                .id(1L)
                .nickname("sdk")
                .build();

        Post post = Post.builder()
                .id(2L)
                .member(member)
                .likes(new ArrayList<>())
                .content("글의 내용")
                .at(LocalDateTime.now())
                .build();

        new PostLike(post, member);

        given(postService.getPostInfo(anyLong()))
                .willReturn(post);

        mvc.perform(
                MockMvcRequestBuilders.get("/api/posts/3")
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.postInfo.likeCount").value(1))
                .andExpect(jsonPath("$.postInfo.id").value(2));
    }

    @Test
    void 글_삭제하기() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute(SessionConst.SESSION_MEMBER_ID, 1L);

        given(postService.delete(anyLong()))
                .willReturn(true);

        mvc.perform(
                MockMvcRequestBuilders.delete("/api/posts/2")
                        .session(session)
                        .header(HeaderConst.authHeader, "TEST_CODE")
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").exists());
    }

}