package com.example.WEEK04.controller;

import com.example.WEEK04.common.ApiResponseDto;
import com.example.WEEK04.common.ResponseFactory;
import com.example.WEEK04.model.dto.request.PostCreateRequest;
import com.example.WEEK04.model.dto.request.PostUpdateRequest;
import com.example.WEEK04.model.dto.response.PostDetailResponse;
import com.example.WEEK04.model.dto.response.PostListResponse;
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
    @Operation(summary = "게시글 작성 API", description = "새로운 게시글을 작성합니다.")
    @PostMapping
    public ResponseEntity<ApiResponseDto<IdData>> create(
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @Valid @RequestBody PostCreateRequest req
    ) {
        Long postId = postService.create(authorization, req);
        return responseFactory.created(new IdData(postId));
    }

    /** 게시글 수정 */
    @Operation(summary = "게시글 수정 API", description = "기존 게시글을 수정합니다.")
    @PatchMapping("/{postId}")
    public ResponseEntity<ApiResponseDto<IdData>> update(
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @PathVariable Long postId,
            @Valid @RequestBody PostUpdateRequest req
    ) {
        Long updatedId = postService.update(authorization, postId, req);
        return responseFactory.ok(new IdData(updatedId));
    }

    /** 게시글 삭제 */
    @Operation(summary = "게시글 삭제 API", description = "게시글을 삭제합니다.")
    @DeleteMapping("/{postId}")
    public ResponseEntity<ApiResponseDto<Void>> delete(
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @PathVariable Long postId
    ) {
        postService.delete(authorization, postId);
        return responseFactory.noContent();
    }

    /** 게시글 목록 조회 */
    @Operation(summary = "게시글 목록 조회 API", description = "전체 게시글 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<ApiResponseDto<PostListResponse>> getPosts(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "desc") String sort
    ) {
        PostListResponse response = postService.getPosts(page, size, sort);
        return responseFactory.ok(response);
    }

    /** 게시글 단건 조회 */
    @Operation(summary = "게시글 단건 조회 API", description = "게시글 ID로 상세 조회합니다.")
    @GetMapping("/{postId}")
    public ResponseEntity<ApiResponseDto<PostDetailResponse>> getPostById(
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @PathVariable Long postId
    ) {
        PostDetailResponse response = postService.getPostById(postId, authorization);
        return responseFactory.ok(response);
    }

    /** 내부용 record DTO */
    record IdData(Long comment_id) { }
}
