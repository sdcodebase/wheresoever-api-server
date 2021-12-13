package wheresoever.quickprotoserver.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import wheresoever.quickprotoserver.api.argumeentresolver.Session;
import wheresoever.quickprotoserver.api.constant.SessionConst;
import wheresoever.quickprotoserver.domain.Member;
import wheresoever.quickprotoserver.domain.Sex;
import wheresoever.quickprotoserver.service.MemberService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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

    /**
     * TODO
     * -  로그인 실패시 throw Exception
     */
    @PostMapping("/login")
    public CreateMemberResponse<String> login(@RequestBody LoginMemberRequest loginRequest, HttpServletRequest request) {
        Member member = memberService.findMemberByEmailAndPassword(loginRequest.getEmail(), loginRequest.getPassword());
        if (member == null) {
            return new CreateMemberResponse<>("NULL");
        }

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

    @Data
    @NoArgsConstructor
    static class LoginMemberRequest {
        private String email;
        private String password;
    }

    @PostMapping
    public CreateMemberResponse<String> signUp(@RequestBody CreateMemberRequest createRequest, HttpServletRequest request) {

        Member member = new Member(createRequest.getEmail(),
                createRequest.getPassword(),
                formatSex(createRequest.getSex()),
                createRequest.getNickname(),
                formatLocalDate(createRequest.getBirthdate()),
                createRequest.getMetropolitan());

        try {
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
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.OK, "해당 이메일은 이미 존재합니다.");
        }
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
        Member member;
        try {
            member = memberService.findMember(memberId);
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.OK, "없는 계정입니다.");
        }

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
            return new UpdateMemberResponse<>(false);
        }

        try {
            memberService.updateMember(memberId,
                    formatSex(request.getSex()),
                    request.getNickname(),
                    formatLocalDate(request.getBirthdate()),
                    request.getMetropolitan());
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.OK, "없는 계정입니다.");
        }

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