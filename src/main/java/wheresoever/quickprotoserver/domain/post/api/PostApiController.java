package wheresoever.quickprotoserver.domain.post.api;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import wheresoever.quickprotoserver.domain.model.Category;
import wheresoever.quickprotoserver.domain.post.application.PostService;
import wheresoever.quickprotoserver.domain.post.domain.Post;
import wheresoever.quickprotoserver.domain.post.dto.request.CreatePostRequest;
import wheresoever.quickprotoserver.domain.post.dto.response.PostDetailDto;
import wheresoever.quickprotoserver.domain.post.dto.response.PostsResponse;
import wheresoever.quickprotoserver.global.argumeentresolver.Session;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostApiController {

    private final PostService postService;

    @GetMapping
    public Page<PostsResponse> readPostList(@RequestParam @Nullable Long memberId, Pageable pageable) {
        return postService.getPostPage(pageable, memberId)
                .map(PostsResponse::of);
    }

    @PostMapping
    public ResponseEntity<Object> createPosts(@Session Long memberId, @RequestBody @Validated CreatePostRequest request) {
        Category category = formatCategory(request.getCategory());

        Long postId = postService.posts(memberId, request.getContent(), category);

        return ResponseEntity.status(HttpStatus.OK).body(Map.of(
                "postId", postId
        ));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<Object> readPostDetail(@PathVariable Long postId) {
        Post postInfo = postService.getPostInfo(postId);
        Integer likeCount = postInfo.getLikes().size();

        return ResponseEntity.status(HttpStatus.OK).body(Map.of(
                "postInfo", PostDetailDto.of(postInfo, likeCount)
        ));
    }

//    @PatchMapping("/{postId}")
//    public ReadPostDetailResponse updatePost(@PathVariable Long postId, @RequestBody CreatePostRequest request) {
//
//
//    }

    // TODO: ?????? memberId??? ??? ????????? ?????? ????????? ??????
    @DeleteMapping("/{postId}")
    public ResponseEntity<Object> deletePost(@Session Long memberId, @PathVariable Long postId) {
        Boolean delete = postService.delete(postId);

        return ResponseEntity.status(HttpStatus.OK).body(Map.of(
                "result", delete
        ));
    }

    // TODO: Util ??????????????? ??????
    public Category formatCategory(String category) {
        Map<String, Category> map = new HashMap<>();
        map.put("??????", Category.MUSIC);
        map.put("??????", Category.ART);
        map.put("??????", Category.SCIENCE);
        map.put("??????", Category.SOCIETY);

        return map.get(category);
    }
}
