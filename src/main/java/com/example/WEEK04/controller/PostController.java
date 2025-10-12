package com.example.WEEK04.controller;

import com.example.WEEK04.model.entity.Post;
import com.example.WEEK04.model.dto.request.PostCreateRequest;
import com.example.WEEK04.model.dto.response.PostListResponse;
import com.example.WEEK04.model.dto.response.PostDetailResponse;
import com.example.WEEK04.model.dto.request.PostUpdateRequest;
import com.example.WEEK04.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {

    // 매직 넘버/문자열 상수 정의
    private static final int DEFAULT_PAGE = 1;
    private static final int DEFAULT_SIZE = 10;
    private static final String DEFAULT_SORT = "desc";

    private final PostService postService;
    public PostController(PostService postService) { this.postService = postService; }

    // 게시글 작성
    @PostMapping
    public ResponseEntity<?> create(
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @RequestBody PostCreateRequest req
    ) {
        Post post = postService.create(authorization, req);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new Response("post_created", new Data(post.getId()), null));
    }

    // 게시글 수정
    @PatchMapping("/{postId}")
    public ResponseEntity<?> update(
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @PathVariable Long postId,
            @RequestBody PostUpdateRequest req
    ) {
        Post updated = postService.update(authorization, postId, req);
        return ResponseEntity.ok(new Response("post_updated", new Data(updated.getId()), null));
    }

    record Data(Long post_id) {}
    record Response(String message, Object data, Object error) {}

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> delete(
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @PathVariable Long postId
    ) {
        postService.delete(authorization, postId);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

    // 게시글 목록 조회
    @GetMapping
    public ResponseEntity<PostListResponse> getPosts(
            @RequestParam(defaultValue = "" + DEFAULT_PAGE) Integer page,
            @RequestParam(defaultValue = "" + DEFAULT_SIZE) Integer size,
            @RequestParam(defaultValue = DEFAULT_SORT) String sort
    ) {
        List<Post> posts = postService.getPosts(page, size, sort);
        int total = postService.getTotalCount();

        return ResponseEntity.ok(new PostListResponse(
                "ok",
                new PostListResponse.Data(posts, page, size, total),
                null
        ));
    }

    // 게시글 단일 조회
    @GetMapping("/{postId}")
    public ResponseEntity<PostDetailResponse> getPostById(@PathVariable Long postId) {
        Post post = postService.getPostById(postId);

        return ResponseEntity.ok(new PostDetailResponse(
                "ok",
                new PostDetailResponse.Data(post),
                null
        ));
    }
}
