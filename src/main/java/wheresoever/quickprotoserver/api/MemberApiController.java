package wheresoever.quickprotoserver.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import wheresoever.quickprotoserver.domain.Member;
import wheresoever.quickprotoserver.domain.Sex;
import wheresoever.quickprotoserver.service.MemberService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberApiController {

    private final MemberService memberService;

    @PostMapping
    public CreateMemberResponse<Long> createMember(@RequestBody CreateMemberRequest request) {

        Member member = new Member(request.getEmail(),
                request.getPassword(),
                formatSex(request.getSex()),
                request.getNickname(),
                formatLocalDate(request.getBirthdate()),
                request.getMetropolitan());

        try {
            Long memberId = memberService.join(member);
            return new CreateMemberResponse<>(memberId); // 추후 sessionToken 발급하는 형식으로 변경하기.
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

    @PatchMapping("/{memberId}")
    public UpdateMemberResponse<Boolean> updateMember(@PathVariable Long memberId, UpdateMemberRequest request) {
        Member member;
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