package wheresoever.quickprotoserver.domain.randommessage.api;

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
import wheresoever.quickprotoserver.domain.randommessage.application.RandomMessageService;
import wheresoever.quickprotoserver.domain.randommessage.domain.RandomMessage;
import wheresoever.quickprotoserver.domain.randommessage.dto.request.SendRmRequest;
import wheresoever.quickprotoserver.domain.randommessage.exception.PrevSentMessageExistException;
import wheresoever.quickprotoserver.global.constant.HeaderConst;
import wheresoever.quickprotoserver.global.constant.SessionConst;
import wheresoever.quickprotoserver.global.error.exception.ErrorInfo;

import java.util.HashMap;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = RandomMessageController.class)
class RandomMessageControllerTest {

    @MockBean
    private RandomMessageService randomMessageService;

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
    void 이미_전송한_경우() throws Exception {
        SendRmRequest request = SendRmRequest.builder()
                .receiverId("2")
                .content("첫 메시지야")
                .build();

        given(randomMessageService.send(anyLong(), anyLong(), anyString()))
                .willThrow(new PrevSentMessageExistException());

        MockHttpSession session = new MockHttpSession();
        session.setAttribute(SessionConst.SESSION_MEMBER_ID, 1L);

        mvc.perform(
                MockMvcRequestBuilders.post("/api/rm")
                        .content(mapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .session(session)
                        .header(HeaderConst.authHeader, "TEST_TOKEN")
        )
                .andExpect(jsonPath("$.code").value(ErrorInfo.PREV_SENT_MESSAGE.getCode()));
    }

    @Test
    void 메시지_전송_성공() throws Exception {
        SendRmRequest request = SendRmRequest.builder()
                .receiverId("2")
                .content("첫 메시지야")
                .build();

        given(randomMessageService.send(anyLong(), anyLong(), anyString()))
                .willReturn(1L);

        MockHttpSession session = new MockHttpSession();
        session.setAttribute(SessionConst.SESSION_MEMBER_ID, 1L);

        mvc.perform(
                MockMvcRequestBuilders.post("/api/rm")
                        .content(mapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .session(session)
                        .header(HeaderConst.authHeader, "TEST_CODE")
        )
                .andExpect(jsonPath("$.messageId").value(1))
                .andExpect(status().isOk());
    }

    @Test
    void 내가_받은_메시지_목록_조회() throws Exception {
        Member sender1 = Member.builder()
                .id(2L)
                .nickname("닉네임1")
                .build();

        Member sender2 = Member.builder()
                .id(3L)
                .nickname("닉네임2")
                .build();

        RandomMessage m1 = RandomMessage.builder()
                .id(4L)
                .sender(sender1)
                .content("반가워")
                .build();

        RandomMessage m2 = RandomMessage.builder()
                .id(5L)
                .sender(sender2)
                .content("안녕")
                .grade(4)
                .build();

        List<RandomMessage> messages = List.of(m1, m2);

        given(randomMessageService.getReceivedMessages(anyLong()))
                .willReturn(messages);

        MockHttpSession session = new MockHttpSession();
        session.setAttribute(SessionConst.SESSION_MEMBER_ID, 1L);
        mvc.perform(
                MockMvcRequestBuilders.get("/api/rm/received")
                        .session(session)
                        .header(HeaderConst.authHeader, "TEST_CODE")
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.messages[0].grade").doesNotExist())
                .andExpect(jsonPath("$.messages[1].grade").value(4))
                .andExpect(jsonPath("$.messages.length()").value(2));
    }

    @Test
    void 내가_보낸_메시지_목록() throws Exception {
        Member receiver1 = Member.builder()
                .id(2L)
                .nickname("닉네임1")
                .build();

        Member receiver2 = Member.builder()
                .id(3L)
                .nickname("닉네임2")
                .build();

        RandomMessage m1 = RandomMessage.builder()
                .id(4L)
                .receiver(receiver1)
                .content("반가워")
                .build();

        RandomMessage m2 = RandomMessage.builder()
                .id(5L)
                .receiver(receiver2)
                .content("안녕")
                .grade(4)
                .build();

        List<RandomMessage> messages = List.of(m1, m2);

        given(randomMessageService.getSentMessages(anyLong()))
                .willReturn(messages);

        MockHttpSession session = new MockHttpSession();
        session.setAttribute(SessionConst.SESSION_MEMBER_ID, 1L);
        mvc.perform(
                MockMvcRequestBuilders.get("/api/rm/sent")
                        .session(session)
                        .header(HeaderConst.authHeader, "TEST_CODE")
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.messages[0].grade").doesNotExist())
                .andExpect(jsonPath("$.messages[1].grade").value(4))
                .andExpect(jsonPath("$.messages.length()").value(2));
    }

    @Test
    void 평가하기() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute(SessionConst.SESSION_MEMBER_ID, 1L);

        HashMap<String, String> request = new HashMap<>();
        request.put("messageId", "2");
        request.put("grade", "4");

        mvc.perform(
                MockMvcRequestBuilders.post("/api/rm/grade")
                        .content(mapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .session(session)
                        .header(HeaderConst.authHeader, "TEST_CODE")
        )
                .andExpect(status().isOk());
    }
}

