package wheresoever.quickprotoserver.domain.comment.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wheresoever.quickprotoserver.domain.comment.application.CommentService;
import wheresoever.quickprotoserver.domain.comment.dto.CommentInfoResponse;
import wheresoever.quickprotoserver.global.argumeentresolver.Session;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/{postId}/comments")
@RestController
public class CommentApiController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<Object> commentToPost(
            @Session Long memberId,
            @PathVariable Long postId,
            @RequestBody Map<String, String> json) {

        commentService.comment(memberId, postId, json.get("content"));

        List<CommentInfoResponse> comments = getComments(postId);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of(
                "comments", comments
        ));
    }

    @GetMapping
    public ResponseEntity<Object> commentsOfPost(
            @PathVariable Long postId
    ) {
        List<CommentInfoResponse> comments = getComments(postId);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of(
                "comments", comments
        ));
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<Object> recomment(
            @Session Long memberId, // TODO: comment.memberId랑 같은지 확인
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @RequestBody Map<String, String> json
    ) {
        commentService.recomment(commentId, json.get("content"));

        List<CommentInfoResponse> comments = getComments(postId);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of(
                "comments", comments
        ));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Object> delete(
            @Session Long memberId, // TODO: comment의 멤버랑 같은지 확인
            @PathVariable Long postId,
            @PathVariable Long commentId
    ) {
        commentService.delete(commentId);

        List<CommentInfoResponse> comments = getComments(postId);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of(
                "comments", comments
        ));
    }

    private List<CommentInfoResponse> getComments(Long postId) {
        return commentService.getCommentsOfPost(postId)
                .stream()
                .map(CommentInfoResponse::of)
                .collect(Collectors.toList());
    }
}
