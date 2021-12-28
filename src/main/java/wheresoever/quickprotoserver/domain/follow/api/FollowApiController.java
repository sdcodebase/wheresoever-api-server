package wheresoever.quickprotoserver.domain.follow.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wheresoever.quickprotoserver.domain.follow.application.FollowService;
import wheresoever.quickprotoserver.domain.follow.domain.Follow;
import wheresoever.quickprotoserver.domain.follow.dto.response.FollowerInfoResponse;
import wheresoever.quickprotoserver.global.argumeentresolver.Session;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class FollowApiController {

    private final FollowService followService;

    @PostMapping("/follow")
    public ResponseEntity followMember(@Session Long followerId, @RequestBody Map<String, String> json) {
        Long memberId = Long.parseLong(json.get("memberId"));
        followService.follow(memberId, followerId);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/following")
    public ResponseEntity<Object> followingList(@Session Long memberId) {
        List<FollowerInfoResponse> following = followService.getFollowings(memberId)
                .stream()
                .map(follow -> FollowerInfoResponse.of(follow.getId(), follow.getMember().getNickname()))
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(Map.of(
                "following", following
        ));
    }

    @GetMapping("/follower")
    public ResponseEntity<Object> followerList(@Session Long memberId) {
        List<FollowerInfoResponse> following = followService.getFollowers(memberId)
                .stream()
                .map(follow -> FollowerInfoResponse.of(follow.getId(), follow.getFollower().getNickname()))
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(Map.of(
                "follower", following
        ));
    }
}
