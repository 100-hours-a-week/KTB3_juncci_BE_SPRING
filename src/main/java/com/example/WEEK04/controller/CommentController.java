package com.example.WEEK04.controller;

import com.example.WEEK04.model.entity.Comment;
import com.example.WEEK04.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts/{postId}/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    //댓글 등록
    @PostMapping
    public ResponseEntity<?> createComment(
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @PathVariable Long postId,
            @RequestBody CommentRequest req
    ) {
        Comment comment = commentService.create(authorization, postId, req.getContent());
        return ResponseEntity.status(201)
                .body(new Response("comment_created", new Data(comment.getId(), comment.getPostId()), null));
    }

    // 댓글 삭제
    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @PathVariable Long postId,
            @PathVariable Long commentId
    ) {
        commentService.delete(authorization, postId, commentId);
        return ResponseEntity.ok(new Response("comment_deleted", new Data(commentId, postId), null));
    }

    // 댓글 목록 조회
    @GetMapping
    public ResponseEntity<?> getComments(@PathVariable Long postId) {
        List<Comment> comments = commentService.getCommentsByPostId(postId);
        return ResponseEntity.ok(new Response("ok", new DataList(comments), null));
    }

    // 댓글 단일 조회
    @GetMapping("/{commentId}")
    public ResponseEntity<?> getComment(@PathVariable Long postId, @PathVariable Long commentId) {
        Comment comment = commentService.getCommentById(postId, commentId);
        return ResponseEntity.ok(
                new Response("ok", new DataForDetail(comment.getId(), comment.getPostId(), comment.getContent(), comment.getCreatedAt()), null)
        );
    }

    // 새 record 추가
    record DataForDetail(Long comment_id, Long post_id, String content, String created_at) {}

    // DTO 내부 클래스
    static class CommentRequest {
        private String content;

        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
    }

    record Response(String message, Object data, Object error) {}
    record Data(Long comment_id, Long post_id) {}
    record DataList(List<Comment> comments) {}
}
