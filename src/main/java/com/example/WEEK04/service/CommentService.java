package com.example.WEEK04.service;

import com.example.WEEK04.exception.BusinessException;
import com.example.WEEK04.exception.ErrorCode;
import com.example.WEEK04.model.dto.response.CommentListResponse;
import com.example.WEEK04.model.dto.response.CommentResponse;
import com.example.WEEK04.model.entity.Comment;
import com.example.WEEK04.model.entity.Post;
import com.example.WEEK04.model.entity.User;
import com.example.WEEK04.repository.CommentRepository;
import com.example.WEEK04.repository.PostRepository;
import com.example.WEEK04.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepo;
    private final PostRepository postRepo;
    private final UserRepository userRepo;

    /** 현재 로그인한 사용자 조회 (subject = userId) */
    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || auth.getName() == null) {
            throw new BusinessException(ErrorCode.AUTH_TOKEN_INVALID);
        }

        Long userId;
        try {
            userId = Long.parseLong(auth.getName());
        } catch (NumberFormatException e) {
            throw new BusinessException(ErrorCode.AUTH_TOKEN_INVALID);
        }

        return userRepo.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
    }

    /** 댓글 작성 */
    public CommentResponse create(Long postId, String content) {
        User user = getCurrentUser();

        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));

        Comment comment = new Comment(post, user, content);
        Comment saved = commentRepo.save(comment);

        post.addComment(saved);

        return new CommentResponse(saved);
    }

    /** 댓글 삭제 */
    public void delete(Long postId, Long commentId) {
        User user = getCurrentUser();

        Comment comment = commentRepo.findById(commentId)
                .orElseThrow(() -> new BusinessException(ErrorCode.COMMENT_NOT_FOUND));

        if (!comment.getPost().getId().equals(postId)) {
            throw new BusinessException(ErrorCode.COMMENT_NOT_FOUND);
        }

        if (!comment.getAuthor().getId().equals(user.getId())) {
            throw new BusinessException(ErrorCode.AUTH_FORBIDDEN);
        }

        comment.getPost().removeComment(comment);
        comment.getAuthor().removeComment(comment);
        commentRepo.delete(comment);
    }

    /** 댓글 목록 조회 */
    @Transactional
    public CommentListResponse getCommentsByPostId(Long postId) {
        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));

        List<Comment> comments = commentRepo.findByPost(post);

        return new CommentListResponse(comments);
    }

    /** 댓글 단건 조회 */
    @Transactional
    public CommentResponse getCommentById(Long postId, Long commentId) {
        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));

        Comment comment = commentRepo.findById(commentId)
                .orElseThrow(() -> new BusinessException(ErrorCode.COMMENT_NOT_FOUND));

        if (!comment.getPost().getId().equals(post.getId())) {
            throw new BusinessException(ErrorCode.COMMENT_NOT_FOUND);
        }

        return new CommentResponse(comment);
    }
}
