package com.example.WEEK04.service;

import com.example.WEEK04.exception.BusinessException;
import com.example.WEEK04.exception.ErrorCode;
import com.example.WEEK04.model.entity.Comment;
import com.example.WEEK04.repository.DummyCommentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    private final DummyCommentRepository repo;

    public CommentService(DummyCommentRepository repo) {
        this.repo = repo;
    }

    // 댓글 등록
    public Comment create(String authorization, Long postId, String content) {
        if (authorization == null || !authorization.startsWith("Bearer ACCESS-TOKEN-")) {
            throw new BusinessException(ErrorCode.AUTH_TOKEN_INVALID);
        }

        if (content == null || content.isBlank()) {
            throw new BusinessException(ErrorCode.COMMENT_FIELD_MISSING);
        }

        Long authorId = Long.parseLong(authorization.replace("Bearer ACCESS-TOKEN-", ""));
        Comment comment = new Comment(null, postId, authorId, content);
        return repo.save(comment);
    }

    // 댓글 삭제
    public void delete(String authorization, Long postId, Long commentId) {
        if (authorization == null || !authorization.startsWith("Bearer ACCESS-TOKEN-")) {
            throw new BusinessException(ErrorCode.AUTH_TOKEN_INVALID);
        }

        Long userId;
        try {
            userId = Long.parseLong(authorization.replace("Bearer ACCESS-TOKEN-", ""));
        } catch (NumberFormatException e) {
            throw new BusinessException(ErrorCode.AUTH_TOKEN_INVALID);
        }

        Comment comment = repo.findById(commentId)
                .orElseThrow(() -> new BusinessException(ErrorCode.COMMENT_NOT_FOUND));

        if (!comment.getPostId().equals(postId)) {
            throw new BusinessException(ErrorCode.COMMENT_NOT_FOUND);
        }

        if (!comment.getAuthorId().equals(userId)) {
            throw new BusinessException(ErrorCode.AUTH_FORBIDDEN);
        }

        boolean deleted = repo.deleteById(commentId);
        if (!deleted) {
            throw new BusinessException(ErrorCode.COMMENT_NOT_FOUND);
        }
    }

    // 댓글 목록 조회
    public List<Comment> getCommentsByPostId(Long postId) {
        return repo.findByPostId(postId);
    }

    // 댓글 단일 조회
    public Comment getCommentById(Long postId, Long commentId) {
        Comment c = repo.findById(commentId)
                .orElseThrow(() -> new BusinessException(ErrorCode.COMMENT_NOT_FOUND));

        if (!c.getPostId().equals(postId)) {
            throw new BusinessException(ErrorCode.COMMENT_NOT_FOUND);
        }

        return c;
    }
}
