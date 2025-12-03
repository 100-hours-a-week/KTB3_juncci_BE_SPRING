package com.example.WEEK04.controller;

import com.example.WEEK04.common.ApiResponseDto;
import com.example.WEEK04.common.ResponseFactory;
import com.example.WEEK04.model.dto.request.PostCreateRequest;
import com.example.WEEK04.model.dto.request.PostUpdateRequest;
import com.example.WEEK04.model.dto.response.PostDetailResponse;
import com.example.WEEK04.model.dto.response.PostListResponse;
import com.example.WEEK04.security.SecurityUtil;
import com.example.WEEK04.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;
    private final ResponseFactory responseFactory;

    public PostController(PostService postService, ResponseFactory responseFactory) {
        this.postService = postService;
        this.responseFactory = responseFactory;
    }

    /** 게시글 작성 */
    @Operation(summary = "게시글 작성 API")
    @PostMapping
    public ResponseEntity<ApiResponseDto<IdData>> create(
            @Valid @RequestBody PostCreateRequest req
    ) {
        Long postId = postService.create(req);
        return responseFactory.created(new IdData(postId));
    }

    /** 게시글 수정 */
    @PatchMapping("/{postId}")
    @Operation(summary = "게시글 수정 API")
    public ResponseEntity<ApiResponseDto<IdData>> update(
            @PathVariable Long postId,
            @Valid @RequestBody PostUpdateRequest req
    ) {
        Long updatedId = postService.update(postId, req);
        return responseFactory.ok(new IdData(updatedId));
    }

    /** 게시글 삭제 */
    @DeleteMapping("/{postId}")
    public ResponseEntity<ApiResponseDto<Void>> delete(
            @PathVariable Long postId
    ) {
        postService.delete(postId);
        return responseFactory.noContent();
    }

    /** 게시글 목록 조회 */
    @GetMapping
    public ResponseEntity<ApiResponseDto<PostListResponse>> getPosts(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "desc") String sort
    ) {
        PostListResponse response = postService.getPosts(page, size, sort);
        return responseFactory.ok(response);
    }

    /** 게시글 상세 조회 */
    @GetMapping("/{postId}")
    public ResponseEntity<ApiResponseDto<PostDetailResponse>> getPostById(
            @PathVariable Long postId
    ) {
        PostDetailResponse response = postService.getPostById(postId);
        return responseFactory.ok(response);
    }

    /** 내가 쓴 게시글 목록 조회 */
    /** 내가 쓴 게시글 목록 조회 */
    @GetMapping("/me")
    @Operation(summary = "내가 작성한 게시글 목록 조회 API")
    public ResponseEntity<ApiResponseDto<PostListResponse>> getMyPosts(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "desc") String sort
    ) {
        PostListResponse response = postService.getMyPosts(page, size, sort);
        return responseFactory.ok(response);
    }

    record IdData(Long post_id) {}
}
