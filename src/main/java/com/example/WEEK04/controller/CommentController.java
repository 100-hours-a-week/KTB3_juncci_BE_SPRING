package com.example.WEEK04.controller;

import com.example.WEEK04.common.ResponseFactory;
import com.example.WEEK04.model.dto.request.CommentCreateRequest;
import com.example.WEEK04.model.dto.response.CommentListResponse;
import com.example.WEEK04.model.dto.response.CommentResponse;
import com.example.WEEK04.model.entity.Comment;
import com.example.WEEK04.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts/{postId}/comments")
public class CommentController {

    private final CommentService commentService;
    private final ResponseFactory responseFactory;

    public CommentController(CommentService commentService, ResponseFactory responseFactory) {
        this.commentService = commentService;
        this.responseFactory = responseFactory;
    }

    // ✅ 댓글 등록
    @PostMapping
    public ResponseEntity<?> createComment(
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @PathVariable Long postId,
            @RequestBody CommentCreateRequest req
    ) {
        Comment comment = commentService.create(authorization, postId, req.getContent());
        return responseFactory.created(new CommentResponse(comment));
    }

    // ✅ 댓글 삭제
    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @PathVariable Long postId,
            @PathVariable Long commentId
    ) {
        commentService.delete(authorization, postId, commentId);
        return responseFactory.noContent();
    }

    // ✅ 댓글 목록 조회
    @GetMapping
    public ResponseEntity<?> getComments(@PathVariable Long postId) {
        List<Comment> comments = commentService.getCommentsByPostId(postId);
        return responseFactory.ok(new CommentListResponse(comments));
    }

    // ✅ 댓글 단일 조회
    @GetMapping("/{commentId}")
    public ResponseEntity<?> getComment(@PathVariable Long postId, @PathVariable Long commentId) {
        Comment comment = commentService.getCommentById(postId, commentId);
        return responseFactory.ok(new CommentResponse(comment));
    }
}
