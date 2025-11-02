package com.example.WEEK04.controller;

import com.example.WEEK04.common.ApiResponseDto;
import com.example.WEEK04.common.ResponseFactory;
import com.example.WEEK04.model.dto.request.CommentCreateRequest;
import com.example.WEEK04.model.dto.response.CommentListResponse;
import com.example.WEEK04.model.dto.response.CommentResponse;
import com.example.WEEK04.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts/{postId}/comments")
public class CommentController {

    private final CommentService commentService;
    private final ResponseFactory responseFactory;

    public CommentController(CommentService commentService, ResponseFactory responseFactory) {
        this.commentService = commentService;
        this.responseFactory = responseFactory;
    }

    /** 댓글 작성 API */
    @Operation(summary = "댓글 작성 API", description = "특정 게시글에 댓글을 작성합니다.")
    @PostMapping
    public ResponseEntity<ApiResponseDto<IdData>> createComment(
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @PathVariable Long postId,
            @Valid @RequestBody CommentCreateRequest req
    ) {
        CommentResponse comment = commentService.create(authorization, postId, req.getContent());
        return responseFactory.created(new IdData(comment.getId()));
    }

    /** 댓글 삭제 API */
    @Operation(summary = "댓글 삭제 API", description = "특정 게시글의 댓글을 삭제합니다.")
    @DeleteMapping("/{commentId}")
    public ResponseEntity<ApiResponseDto<Void>> deleteComment(
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @PathVariable Long postId,
            @PathVariable Long commentId
    ) {
        commentService.delete(authorization, postId, commentId);
        return responseFactory.noContent();
    }

    /** 댓글 목록 조회 API */
    @Operation(summary = "댓글 목록 조회 API", description = "게시글 ID로 모든 댓글 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<ApiResponseDto<CommentListResponse>> getComments(@PathVariable Long postId) {
        CommentListResponse response = commentService.getCommentsByPostId(postId);
        return responseFactory.ok(response);
    }

    /** 댓글 단건 조회 API */
    @Operation(summary = "댓글 단건 조회 API", description = "댓글 ID로 특정 댓글의 상세 정보를 조회합니다.")
    @GetMapping("/{commentId}")
    public ResponseEntity<ApiResponseDto<CommentResponse>> getComment(
            @PathVariable Long postId,
            @PathVariable Long commentId
    ) {
        CommentResponse comment = commentService.getCommentById(postId, commentId);
        return responseFactory.ok(comment);
    }

    /** 내부용 레코드 DTO */
    record IdData(Long comment_id) { }
}
