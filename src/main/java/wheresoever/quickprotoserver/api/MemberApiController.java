package wheresoever.quickprotoserver.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wheresoever.quickprotoserver.domain.Member;
import wheresoever.quickprotoserver.service.MemberService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberApiController {

    private final MemberService memberService;

    @PostMapping
    public GenerateMemberResponse<Long> generateMember(@RequestBody GenerateMemberRequest request) {
        Member member = new Member(request.getName(), request.getAge(), request.getIntroduce());
        Long memberId = memberService.join(member);

        return new GenerateMemberResponse<>(memberId); // 추후 token 발급하는 형식으로!
    }

    @Data
    static class GenerateMemberRequest {
        private String name;
        private int age;
        private String introduce;
    }

    @Data
    @AllArgsConstructor
    static class GenerateMemberResponse<T> {
        private T id;
    }
}
