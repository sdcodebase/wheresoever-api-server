package wheresoever.quickprotoserver.domain.post.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import wheresoever.quickprotoserver.domain.model.Category;
import wheresoever.quickprotoserver.domain.post.domain.Post;
import wheresoever.quickprotoserver.domain.post.dto.PostDetailDto;
import wheresoever.quickprotoserver.domain.post.application.PostService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostApiController {

    private final PostService postService;

    @GetMapping
    public Page<ReadPostsResponse> readPostList(@RequestParam @Nullable Long memberId, Pageable pageable) {
        return postService.getPostPage(pageable, memberId).map(ReadPostsResponse::new);
    }

    @Data
    @AllArgsConstructor
    static class ReadPostsResponse {
        private String content;
        private String likeCount;
        private String commentCount;
        private String memberId;
        private String nickName;
        private String id;
        private String category;

        public ReadPostsResponse(Post post) {
            content = post.getContent();
            likeCount = Long.toString(post.getLikes().size());
//            commentCount = Long.toString(post.getComments().stream().mapToInt(comment -> comment.getCommentChildren().size()).sum());
            memberId = post.getMember().getId().toString();
            nickName = post.getMember().getNickname();
            id = Long.toString(post.getId());
            category = post.getCategory().toString();
        }
    }

    @PostMapping
    public CreatePostResponse createPosts(@RequestBody CreatePostRequest request) {

        Category category = formatCategory(request.getCategory());

        try {
            // 파라미터로 넘길 DTO도 필요할듯..? 엄청 많아질 수도
            Long postId = postService.posts(request.getMemberId(), request.getContent(), category);
            return new CreatePostResponse(postId);

        } catch (IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.OK, "없는 계정입니다.");
        }
    }

    @Data
    @AllArgsConstructor
    static class CreatePostRequest {
        private Long memberId;
        private String content;
        private String category;
    }

    @Data
    @AllArgsConstructor
    static class CreatePostResponse<T> {
        private T id;
    }

//    @GetMapping("/{postId}")
//    public PostDetailDto readPostDetail(@PathVariable Long postId) {
//        return postService.getPostDetailInfo(postId);
//    }

    @Data
    @AllArgsConstructor
    static class ReadPostDetailResponse {

    }

//    @PatchMapping("/{postId}")
//    public ReadPostDetailResponse updatePost(@PathVariable Long postId, @RequestBody CreatePostRequest request) {
//
//
//    }

    @DeleteMapping("/{postId}")
    public DeletePostResponse<Long> deletePost(@PathVariable Long postId) {
        try {
            Boolean aBoolean = postService.delete(postId);
            return new DeletePostResponse(aBoolean);
        } catch (IllegalStateException e) {
            // 만약, 이미 삭제된 거라면 어떻게 처리..?
            throw new ResponseStatusException(HttpStatus.OK, "없는 게시글입니다.");
        }
    }

    @Data
    @AllArgsConstructor
    static class DeletePostResponse<T> {
        private T ok;
    }


    public Category formatCategory(String category) {
        Map<String, Category> map = new HashMap<>();
        map.put("음악", Category.MUSIC);
        map.put("예술", Category.ART);
        map.put("과학", Category.SCIENCE);
        map.put("사회", Category.SOCIETY);

        return map.get(category);
    }
}
