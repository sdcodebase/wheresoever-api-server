package wheresoever.quickprotoserver.domain.comment.api;

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
import wheresoever.quickprotoserver.domain.comment.application.CommentService;
import wheresoever.quickprotoserver.domain.comment.domain.Comment;
import wheresoever.quickprotoserver.domain.member.domain.Member;
import wheresoever.quickprotoserver.domain.post.exception.PostNotFoundException;
import wheresoever.quickprotoserver.global.constant.HeaderConst;
import wheresoever.quickprotoserver.global.constant.SessionConst;
import wheresoever.quickprotoserver.global.error.exception.ErrorInfo;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = CommentApiController.class)
class CommentApiControllerTest {

    @MockBean
    private CommentService commentService;

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
    void 없는_글에_댓글_시도() throws Exception {
        given(commentService.comment(anyLong(), anyLong(), anyString()))
                .willThrow(new PostNotFoundException());

        MockHttpSession session = new MockHttpSession();
        session.setAttribute(SessionConst.SESSION_MEMBER_ID, 1L);

        HashMap<String, String> request = new HashMap<>();
        request.put("content", "Fadsjkfjakl");

        mvc.perform(
                MockMvcRequestBuilders.post("/api/1/comments")
                        .content(mapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .session(session)
                        .header(HeaderConst.authHeader, "TEST_TOKEN")
        )
                .andExpect(jsonPath("$.code").value(ErrorInfo.POST_NOT_FOUND.getCode()));
    }

    @Test
    void 댓글_목록_조회() throws Exception {

        Member member = Member.builder()
                .id(1L)
                .nickname("sdk")
                .build();

        Comment comment1 = Comment.builder()
                .id(2L)
                .content("1번댓글")
                .member(member)
                .at(LocalDateTime.now())
                .build();

        Comment comment2 = Comment.builder()
                .id(3L)
                .content("1번댓글")
                .member(member)
                .at(LocalDateTime.now())
                .build();

        given(commentService.getCommentsOfPost(anyLong()))
                .willReturn(List.of(comment1, comment2));


        mvc.perform(
                MockMvcRequestBuilders.get("/api/1/comments")
        )
                .andExpect(jsonPath("$.comments").isArray());

    }
}