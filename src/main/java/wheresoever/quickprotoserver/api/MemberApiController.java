package wheresoever.quickprotoserver.api;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import wheresoever.quickprotoserver.domain.Member;
import wheresoever.quickprotoserver.service.MemberService;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    @PostMapping("/api/members")
    public Long generateMember(@RequestBody GenerateMemberRequest request) {
        Member member = new Member(request.getName(), request.getAge(), request.getIntroduce());
        return memberService.join(member);
    }

    @Data
    static class GenerateMemberRequest {
        String name;
        int age;
        String introduce;
    }
}
