package wheresoever.quickprotoserver.domain.member.api;

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
import wheresoever.quickprotoserver.domain.member.application.MemberService;
import wheresoever.quickprotoserver.domain.member.domain.Member;
import wheresoever.quickprotoserver.domain.member.dto.request.CreateMemberRequest;
import wheresoever.quickprotoserver.domain.member.dto.request.LoginMemberRequest;
import wheresoever.quickprotoserver.domain.member.dto.request.UpdateMemberRequest;
import wheresoever.quickprotoserver.domain.member.exception.MemberLoginFailException;
import wheresoever.quickprotoserver.global.constant.HeaderConst;
import wheresoever.quickprotoserver.global.constant.SessionConst;
import wheresoever.quickprotoserver.global.error.exception.ErrorInfo;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = MemberApiController.class)
class MemberApiControllerTest {

    @MockBean
    private MemberService memberService;

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
    void 세션이_유효하지_않은_경우() throws Exception {
        UpdateMemberRequest request = UpdateMemberRequest.builder()
                .nickname("sdk")
                .sex("여성")
                .birthdate("20190101")
                .metropolitan("서울")
                .build();

        mvc.perform(
                MockMvcRequestBuilders.patch("/api/members/1L")
                        .content(mapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HeaderConst.authHeader, "fladskafskjmd")
        ).andExpect(jsonPath("$.code").value(ErrorInfo.SESSION_NOT_FOUND.getCode()));
    }

    @Test
    void 세션이_유효한_경우() throws Exception {
        UpdateMemberRequest request = UpdateMemberRequest.builder()
                .nickname("sdk")
                .sex("여성")
                .birthdate("20190101")
                .metropolitan("서울")
                .build();

        MockHttpSession session = new MockHttpSession();
        session.setAttribute(SessionConst.SESSION_MEMBER_ID, 1L);

        mvc.perform(
                MockMvcRequestBuilders.patch("/api/members/1")
                        .content(mapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .session(session)
                        .header(HeaderConst.authHeader, "TEST_TOKEN")
        ).andExpect(status().isOk());
    }

    @Test
    void 세션은_유효한데_권한은_없는_경우() throws Exception {
        UpdateMemberRequest request = UpdateMemberRequest.builder()
                .nickname("sdk")
                .sex("여성")
                .birthdate("20190101")
                .metropolitan("서울")
                .build();

        MockHttpSession session = new MockHttpSession();
        session.setAttribute(SessionConst.SESSION_MEMBER_ID, 1L);

        mvc.perform(// session의 MemberId가 다름
                MockMvcRequestBuilders.patch("/api/members/2")
                        .content(mapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .session(session)
                        .header(HeaderConst.authHeader, "TEST_TOKEN")
        ).andExpect(jsonPath("$.code").value(ErrorInfo.MEMBER_NOT_FOUND.getCode()));
    }

    @Test
    void 로그인_실패() throws Exception {
        LoginMemberRequest request = LoginMemberRequest.builder()
                .email("sdkim@gmail.com")
                .password("dafsd")
                .build();

        given(memberService.validateMemberLogin(anyString(), anyString()))
                .willThrow(new MemberLoginFailException());

        mvc.perform(
                MockMvcRequestBuilders.post("/api/members/login")
                        .content(mapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(jsonPath("$.code").value(ErrorInfo.MEMBER_LOGIN_FAIL.getCode()));
    }

    @Test
    /**
     * TODO: RedisHttpSession을 이용해서 테스트
     */

    void 로그인_성공() throws Exception {
        LoginMemberRequest request = LoginMemberRequest.builder()
                .email("sdkim@gmail.com")
                .password("Adsfds")
                .build();

        Member member = Member.builder()
                .id(7L)
                .build();

        given(memberService.validateMemberLogin(anyString(), anyString()))
                .willReturn(member);

        mvc.perform(
                MockMvcRequestBuilders.post("/api/members/login")
                        .content(mapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(jsonPath("$.token").exists());
    }

    /**
     * TODO: RedisHttpSession을 이용해서 테스트
     */
    @Test
    void 회원가입() throws Exception {

        CreateMemberRequest request = CreateMemberRequest.builder()
                .email("sdkim@gmail.com")
                .password("faljdsk")
                .build();

        given(memberService.join(any()))
                .willReturn(1L);

        mvc.perform(
                MockMvcRequestBuilders.post("/api/members")
                        .content(mapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(jsonPath("$.token").exists());
    }
}