package wheresoever.quickprotoserver.domain.follow.api;


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
import wheresoever.quickprotoserver.domain.follow.domain.Follow;
import wheresoever.quickprotoserver.domain.follow.exception.AlreadyFollowedException;
import wheresoever.quickprotoserver.domain.follow.application.FollowService;
import wheresoever.quickprotoserver.domain.member.domain.Member;
import wheresoever.quickprotoserver.global.constant.HeaderConst;
import wheresoever.quickprotoserver.global.constant.SessionConst;
import wheresoever.quickprotoserver.global.error.exception.ErrorInfo;

import java.util.HashMap;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = FollowApiController.class)
class FollowApiControllerTest {

    @MockBean
    private FollowService followService;

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
    void 이미_팔로우_한_경우() throws Exception {
        given(followService.follow(anyLong(), anyLong()))
                .willThrow(new AlreadyFollowedException());

        MockHttpSession session = new MockHttpSession();
        session.setAttribute(SessionConst.SESSION_MEMBER_ID, 1L);

        HashMap<String, String> request = new HashMap<>();
        request.put("memberId", "2");
        mvc.perform(
                MockMvcRequestBuilders.post("/api/follow")
                        .content(mapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .session(session)
                        .header(HeaderConst.authHeader, "TEST_TOKEN")
        )
                .andExpect(jsonPath("$.code").value(ErrorInfo.PREV_FOLLOWED.getCode()));
    }

    @Test
    void 팔로잉_목록() throws Exception {

        Member follower1 = Member.builder()
                .id(2L)
                .nickname("ksd")
                .build();

        Member follower2 = Member.builder()
                .id(3L)
                .nickname("fasd")
                .build();

        Member member = Member.builder()
                .id(4L)
                .nickname("afds")
                .build();


        Follow follow1 = Follow.builder()
                .id(5L)
                .follower(follower1)
                .member(member)
                .build();

        Follow follow2 = Follow.builder()
                .id(6L)
                .follower(follower2)
                .member(member)
                .build();


        given(followService.getFollowings(anyLong()))
                .willReturn(List.of(follow1, follow2));

        MockHttpSession session = new MockHttpSession();
        session.setAttribute(SessionConst.SESSION_MEMBER_ID, 1L);
        mvc.perform(
                MockMvcRequestBuilders.get("/api/following")
                        .session(session)
                        .header(HeaderConst.authHeader, "TEST_TOKEN")
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.following").isArray());
    }

    @Test
    void 팔로워_목록() throws Exception {

        Member follower1 = Member.builder()
                .id(2L)
                .nickname("ksd")
                .build();

        Member follower2 = Member.builder()
                .id(3L)
                .nickname("fasd")
                .build();

        Member member = Member.builder()
                .id(4L)
                .nickname("afds")
                .build();


        Follow follow1 = Follow.builder()
                .id(5L)
                .follower(follower1)
                .member(member)
                .build();

        Follow follow2 = Follow.builder()
                .id(6L)
                .follower(follower2)
                .member(member)
                .build();

        // DB에서 나온 결과를 이렇게 다 입력해주는게 맞을까?
        // 어디까지 mocking하는게 맞을까

        given(followService.getFollowers(anyLong()))
                .willReturn(List.of(follow1, follow2));

        MockHttpSession session = new MockHttpSession();
        session.setAttribute(SessionConst.SESSION_MEMBER_ID, 1L);
        mvc.perform(
                MockMvcRequestBuilders.get("/api/follower")
                        .session(session)
                        .header(HeaderConst.authHeader, "TEST_TOKEN")
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.follower").isArray());

    }
}