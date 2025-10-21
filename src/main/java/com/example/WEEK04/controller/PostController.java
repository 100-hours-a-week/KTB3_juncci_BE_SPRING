package com.example.WEEK04.controller;

import com.example.WEEK04.common.ApiResponseDto;
import com.example.WEEK04.common.ResponseFactory;
import com.example.WEEK04.model.dto.request.PostCreateRequest;
import com.example.WEEK04.model.dto.request.PostUpdateRequest;
import com.example.WEEK04.model.entity.Post;
import com.example.WEEK04.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;
    private final ResponseFactory responseFactory;

    public PostController(PostService postService, ResponseFactory responseFactory) {
        this.postService = postService;
        this.responseFactory = responseFactory;
    }

    @Operation(summary = "게시글 작성 API", description = "새로운 게시글을 작성합니다.")
    @PostMapping
    public ResponseEntity<ApiResponseDto<IdData>> create(
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @Valid @RequestBody PostCreateRequest req
    ) {
        Post post = postService.create(authorization, req);
        return responseFactory.created(new IdData(post.getId()));
    }

    @Operation(summary = "게시글 수정 API", description = "기존 게시글을 수정합니다.")
    @PatchMapping("/{postId}")
    public ResponseEntity<ApiResponseDto<IdData>> update(
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @PathVariable Long postId,
            @Valid @RequestBody PostUpdateRequest req
    ) {
        Post updated = postService.update(authorization, postId, req);
        return responseFactory.ok(new IdData(updated.getId()));
    }

    @Operation(summary = "게시글 삭제 API", description = "게시글을 삭제합니다.")
    @DeleteMapping("/{postId}")
    public ResponseEntity<ApiResponseDto<Void>> delete(
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @PathVariable Long postId
    ) {
        postService.delete(authorization, postId);
        return responseFactory.noContent();
    }

    @Operation(summary = "게시글 목록 조회 API", description = "전체 게시글 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<ApiResponseDto<List<Post>>> getPosts(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "desc") String sort
    ) {
        List<Post> posts = postService.getPosts(page, size, sort);
        return responseFactory.ok(posts);
    }

    @Operation(summary = "게시글 단건 조회 API", description = "게시글 ID로 상세 조회합니다.")
    @GetMapping("/{postId}")
    public ResponseEntity<ApiResponseDto<Post>> getPostById(@PathVariable Long postId) {
        Post post = postService.getPostById(postId);
        return responseFactory.ok(post);
    }

    record IdData(Long post_id) {}
}
