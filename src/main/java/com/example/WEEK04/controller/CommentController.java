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
    @Operation(summary = "댓글 작성 API")
    @PostMapping
    public ResponseEntity<ApiResponseDto<IdData>> createComment(
            @PathVariable Long postId,
            @Valid @RequestBody CommentCreateRequest req
    ) {
        CommentResponse comment = commentService.create(postId, req.getContent());
        return responseFactory.created(new IdData(comment.getId()));
    }

    /** 댓글 삭제 API */
    @Operation(summary = "댓글 삭제 API")
    @DeleteMapping("/{commentId}")
    public ResponseEntity<ApiResponseDto<Void>> deleteComment(
            @PathVariable Long postId,
            @PathVariable Long commentId
    ) {
        commentService.delete(postId, commentId);
        return responseFactory.noContent();
    }

    /** 댓글 목록 조회 API */
    @GetMapping
    public ResponseEntity<ApiResponseDto<CommentListResponse>> getComments(
            @PathVariable Long postId
    ) {
        CommentListResponse response = commentService.getCommentsByPostId(postId);
        return responseFactory.ok(response);
    }

    /** 댓글 단건 조회 API */
    @GetMapping("/{commentId}")
    public ResponseEntity<ApiResponseDto<CommentResponse>> getComment(
            @PathVariable Long postId,
            @PathVariable Long commentId
    ) {
        CommentResponse comment = commentService.getCommentById(postId, commentId);
        return responseFactory.ok(comment);
    }

    /** 내부용 DTO */
    record IdData(Long comment_id) {}
}
