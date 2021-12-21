package wheresoever.quickprotoserver.domain.member.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import wheresoever.quickprotoserver.domain.member.application.MemberService;
import wheresoever.quickprotoserver.domain.member.domain.Member;
import wheresoever.quickprotoserver.domain.member.dto.request.LoginMemberRequest;
import wheresoever.quickprotoserver.domain.member.exception.MemberNotFoundException;
import wheresoever.quickprotoserver.domain.model.Sex;
import wheresoever.quickprotoserver.global.argumeentresolver.Session;
import wheresoever.quickprotoserver.global.constant.SessionConst;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
@Slf4j
public class MemberApiController {

    private final MemberService memberService;

    @PostMapping("/login")
    public CreateMemberResponse<String> login(@Valid @RequestBody LoginMemberRequest loginRequest, HttpServletRequest request) {
        Member member = memberService.validateMemberLogin(loginRequest.getEmail(), loginRequest.getPassword());

        HttpSession prevSession = request.getSession(false);

        if (prevSession != null) {
            // 이미 해당 세션이 있는데 로그인 시도하는 경우 --> 이전 세션 삭제
            // 중복로그인을 막을 수 있다.
            log.info("There is valid Session. expire previous session: {}", prevSession.getId());
            prevSession.invalidate();
        }

        HttpSession newSession = request.getSession();
        newSession.setAttribute(SessionConst.SESSION_MEMBER_ID, member.getId());

        String sessionId = newSession.getId();
        log.info("Issue session: {}", sessionId);

        return new CreateMemberResponse<>(sessionId);
    }


    @PostMapping
    public CreateMemberResponse<String> signUp(@RequestBody CreateMemberRequest createRequest, HttpServletRequest request) {

        Member member = new Member(createRequest.getEmail(),
                createRequest.getPassword(),
                formatSex(createRequest.getSex()),
                createRequest.getNickname(),
                formatLocalDate(createRequest.getBirthdate()),
                createRequest.getMetropolitan());

        Long memberId = memberService.join(member);

        // 혹시 쿠키에 있는 세션토큰이 유효하면 삭제 --> 자동으로 로그인 처리 되기 때문에 이전 계정 로그아웃
        HttpSession currentSession = request.getSession(false);
        if (currentSession != null) {
            log.info("Expire current session: {}", currentSession.getId());
            currentSession.invalidate();
        }

        HttpSession newSession = request.getSession();
        newSession.setAttribute(SessionConst.SESSION_MEMBER_ID, memberId);

        log.info("Issue session: {}", newSession.getId());

        return new CreateMemberResponse<>(newSession.getId());
    }

    @Data
    @NoArgsConstructor
    static class CreateMemberRequest {
        private String email;
        private String password;
        private String sex;
        private String birthdate;
        private String nickname;
        private String metropolitan;
    }

    @Data
    @AllArgsConstructor
    static class CreateMemberResponse<T> {
        private T token;
    }

    @GetMapping("/{memberId}")
    public ReadMemberResponse readMember(@PathVariable Long memberId) {
        Member member = memberService.findMember(memberId);

        return new ReadMemberResponse(
                formatSex(member.getSex()),
                member.getNickname(),
                formatLocalDate(member.getBirthdate()),
                member.getMetropolitan()
        );
    }

    @Data
    @AllArgsConstructor
    static class ReadMemberResponse {
        private String sex;
        private String nickname;
        private String birthdate;
        private String metropolitan;
    }

    /**
     * TODO
     * sessionMemberId != memberId인 경우 throw Exception --> 공통처리로직 고안 (AOP, Interceptor, resolver...)
     */
    @PatchMapping("/{memberId}")
    public UpdateMemberResponse<Boolean> updateMember(@Session Long sessionMemberId, @PathVariable Long memberId
            , @RequestBody UpdateMemberRequest request) {

        if (!sessionMemberId.equals(memberId)) {
            log.error("mismatch session[{}] and pathParams[{}]", sessionMemberId, memberId);
            throw new MemberNotFoundException();
        }

        memberService.updateMember(memberId,
                formatSex(request.getSex()),
                request.getNickname(),
                formatLocalDate(request.getBirthdate()),
                request.getMetropolitan());

        return new UpdateMemberResponse<>(true);
    }

    @Data
    @NoArgsConstructor
    static class UpdateMemberRequest {
        private String sex;
        private String nickname;
        private String birthdate;
        private String metropolitan;
    }

    @Data
    @AllArgsConstructor
    static class UpdateMemberResponse<T> {
        private T ok;
    }

    @Data
    @AllArgsConstructor
    static class DeleteMemberResponse<T> {
        private T ok;
    }

    public Sex formatSex(String sex) {
        Map<String, Sex> sexMap = new HashMap<>();
        sexMap.put("여성", Sex.FEMALE);
        sexMap.put("남성", Sex.MALE);
        return sexMap.get(sex);
    }

    public String formatSex(Sex sex) {
        Map<Sex, String> sexMap = new HashMap<>();
        sexMap.put(Sex.FEMALE, "여성");
        sexMap.put(Sex.MALE, "남성");
        return sexMap.get(sex);
    }

    public LocalDate formatLocalDate(String birthdate) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        return LocalDate.parse(birthdate, dateTimeFormatter);
    }

    public String formatLocalDate(LocalDate birthdate) {
        return birthdate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }
}