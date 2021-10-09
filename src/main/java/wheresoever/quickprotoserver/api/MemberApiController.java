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
    public GenerateMemberResponse<Long> generateMember(@RequestBody MemberSaveDto request) {
        Map<String, Sex> sexMap = new HashMap<>();
        sexMap.put("여성", Sex.FEMALE);
        sexMap.put("남성", Sex.MALE);

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate birthdate = LocalDate.parse(request.getBirthdate(), dateTimeFormatter);

        Member member = new Member(request.getEmail(),
                request.getPassword(),
                sexMap.get(request.getSex()),
                request.getNickname(), birthdate,
                request.getMetropolitan());

        try {
            Long memberId = memberService.join(member);
            return new GenerateMemberResponse<>(memberId); // 추후 sessionToken 발급하는 형식으로 변경하기.
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.OK, "해당 이메일은 이미 존재합니다.");
        }
    }

    @Data
    @NoArgsConstructor
    static class MemberSaveDto {
        private String email;
        private String password;
        private String sex;
        private String birthdate;
        private String nickname;
        private String metropolitan;
    }

    @Data
    @AllArgsConstructor
    static class GenerateMemberResponse<T> {
        private T token;
    }

    @DeleteMapping("/{memberId}")
    public WithdrawnMemberResponse<Boolean> withdrawnMember(@PathVariable Long memberId, String reason) {
        Boolean state = memberService.withdrawnMember(memberId, reason);
        return new WithdrawnMemberResponse<>(state);
    }

    @Data
    @AllArgsConstructor
    static class WithdrawnMemberResponse<T>{
        private T ok;
    }

}
