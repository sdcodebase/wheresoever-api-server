package wheresoever.quickprotoserver.domain.postlike.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wheresoever.quickprotoserver.domain.postlike.application.PostLikeService;
import wheresoever.quickprotoserver.domain.postlike.domain.PostLike;
import wheresoever.quickprotoserver.domain.postlike.dto.PostLikeListResponse;
import wheresoever.quickprotoserver.global.argumeentresolver.Session;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
@Slf4j
public class PostLikeApiController {

    private final PostLikeService postLikeService;

    @PostMapping("/{postId}/like")
    public ResponseEntity likeToPost(@PathVariable Long postId, @Session Long memberId) {
        postLikeService.like(memberId, postId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{postId}/like")
    public ResponseEntity<Object> likeListOfPost(@PathVariable Long postId) {

        List<PostLike> likes = postLikeService.getLikes(postId);

        List<PostLikeListResponse> response = likes.stream()
                .map(PostLikeListResponse::from)
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(Map.of("likes", response));
    }
}
