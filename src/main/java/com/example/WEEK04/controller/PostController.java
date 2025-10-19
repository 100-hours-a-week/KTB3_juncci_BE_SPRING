package com.example.WEEK04.controller;

import com.example.WEEK04.common.ResponseFactory;
import com.example.WEEK04.model.entity.Post;
import com.example.WEEK04.model.dto.request.PostCreateRequest;
import com.example.WEEK04.model.dto.request.PostUpdateRequest;
import com.example.WEEK04.model.dto.response.PostListResponse;
import com.example.WEEK04.model.dto.response.PostDetailResponse;
import com.example.WEEK04.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {

    // ✅ 매직 넘버/문자열 상수 정의
    private static final int DEFAULT_PAGE = 1;
    private static final int DEFAULT_SIZE = 10;
    private static final String DEFAULT_SORT = "desc";

    private final PostService postService;
    private final ResponseFactory responseFactory; // ✅ 추가

    public PostController(PostService postService, ResponseFactory responseFactory) {
        this.postService = postService;
        this.responseFactory = responseFactory;
    }

    // ✅ 게시글 작성
    @PostMapping
    public ResponseEntity<?> create(
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @RequestBody PostCreateRequest req
    ) {
        Post post = postService.create(authorization, req);
        return responseFactory.created(new IdData(post.getId()));
    }

    // ✅ 게시글 수정
    @PatchMapping("/{postId}")
    public ResponseEntity<?> update(
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @PathVariable Long postId,
            @RequestBody PostUpdateRequest req
    ) {
        Post updated = postService.update(authorization, postId, req);
        return responseFactory.ok(new IdData(updated.getId()));
    }

    // ✅ 게시글 삭제
    @DeleteMapping("/{postId}")
    public ResponseEntity<?> delete(
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @PathVariable Long postId
    ) {
        postService.delete(authorization, postId);
        return responseFactory.noContent();
    }

    // ✅ 게시글 목록 조회
    @GetMapping
    public ResponseEntity<?> getPosts(
            @RequestParam(defaultValue = "" + DEFAULT_PAGE) Integer page,
            @RequestParam(defaultValue = "" + DEFAULT_SIZE) Integer size,
            @RequestParam(defaultValue = DEFAULT_SORT) String sort
    ) {
        List<Post> posts = postService.getPosts(page, size, sort);
        int total = postService.getTotalCount();

        return responseFactory.ok(
                new PostListResponse.Data(posts, page, size, total)
        );
    }

    // ✅ 게시글 단일 조회
    @GetMapping("/{postId}")
    public ResponseEntity<?> getPostById(@PathVariable Long postId) {
        Post post = postService.getPostById(postId);
        return responseFactory.ok(new PostDetailResponse.Data(post));
    }

    // ✅ 내부 record (응답용)
    record IdData(Long post_id) {}
}
