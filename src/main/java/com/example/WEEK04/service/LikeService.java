package com.example.WEEK04.service;

import com.example.WEEK04.exception.*;
import com.example.WEEK04.model.dto.response.*;
import com.example.WEEK04.model.entity.*;
import com.example.WEEK04.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LikeService {

    private final LikeRepository likeRepo;
    private final PostRepository postRepo;
    private final UserRepository userRepo;
    private final AuthService authService;

    public LikeService(LikeRepository likeRepo, PostRepository postRepo,
                       UserRepository userRepo, AuthService authService) {
        this.likeRepo = likeRepo;
        this.postRepo = postRepo;
        this.userRepo = userRepo;
        this.authService = authService;
    }

    /** 좋아요 추가 */
    public LikeActionResponse like(String authorization, Long postId) {
        Long userId = authService.extractUserId(authorization);

        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.AUTH_FORBIDDEN));

        if (likeRepo.existsByPostIdAndUserId(postId, userId))
            throw new BusinessException(ErrorCode.LIKE_ALREADY_EXISTS);

        Like like = new Like(post, user);
        post.addLike(like);
        user.addLike(like);

        likeRepo.save(like);

        return new LikeActionResponse(postId, "좋아요 추가 성공");
    }

    /** 좋아요 취소 */
    public LikeActionResponse unlike(String authorization, Long postId) {
        Long userId = authService.extractUserId(authorization);

        Like like = likeRepo.findByPostIdAndUserId(postId, userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.LIKE_NOT_FOUND));

        Post post = like.getPost();
        User user = like.getUser();

        post.removeLike(like);
        user.removeLike(like);

        likeRepo.delete(like);

        return new LikeActionResponse(postId, "좋아요 취소 성공");
    }

    /** 좋아요 개수 조회 */
    @Transactional(readOnly = true)
    public LikeCountResponse getLikeCount(Long postId) {
        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));
        int count = likeRepo.countByPostId(postId);
        return new LikeCountResponse(postId, count);
    }
}
