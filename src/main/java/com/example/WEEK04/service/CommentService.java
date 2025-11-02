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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CommentService {

    private final CommentRepository commentRepo;
    private final PostRepository postRepo;
    private final UserRepository userRepo;
    private final AuthService authService;

    public CommentService(CommentRepository commentRepo,
                          PostRepository postRepo,
                          UserRepository userRepo,
                          AuthService authService) {
        this.commentRepo = commentRepo;
        this.postRepo = postRepo;
        this.userRepo = userRepo;
        this.authService = authService;
    }

    /** 댓글 작성 */
    public CommentResponse create(String authorization, Long postId, String content) {
        Long userId = authService.extractUserId(authorization);
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));

        Comment comment = new Comment(post, user, content);
        Comment saved = commentRepo.save(comment);

        post.addComment(saved);

        return new CommentResponse(saved);
    }

    /** 댓글 삭제 */
    public void delete(String authorization, Long postId, Long commentId) {
        Long userId = authService.extractUserId(authorization);
        Comment comment = commentRepo.findById(commentId)
                .orElseThrow(() -> new BusinessException(ErrorCode.COMMENT_NOT_FOUND));

        if (!comment.getPost().getId().equals(postId)) {
            throw new BusinessException(ErrorCode.COMMENT_NOT_FOUND);
        }

        if (!comment.getAuthor().getId().equals(userId)) {
            throw new BusinessException(ErrorCode.AUTH_FORBIDDEN);
        }

        comment.getPost().removeComment(comment);
        comment.getAuthor().removeComment(comment);
        commentRepo.delete(comment);
    }

    /** 댓글 목록 조회 */
    @Transactional(readOnly = true)
    public CommentListResponse getCommentsByPostId(Long postId) {
        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));

        List<Comment> comments = commentRepo.findByPost(post);

        // 기존에는 (message, id, list) 로 만들었는데 → List<Comment> 만 넘기면 됨
        return new CommentListResponse(comments);
    }

    /** 댓글 단건 조회 */
    @Transactional(readOnly = true)
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
