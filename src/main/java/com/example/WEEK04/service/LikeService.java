package com.example.WEEK04.service;

import com.example.WEEK04.exception.BusinessException;
import com.example.WEEK04.exception.ErrorCode;
import com.example.WEEK04.repository.DummyLikeRepository;
import org.springframework.stereotype.Service;

@Service
public class LikeService {

    private final DummyLikeRepository repo;
    private final AuthService authService;

    public LikeService(DummyLikeRepository repo, AuthService authService) {
        this.repo = repo;
        this.authService = authService;
    }


    // 좋아요 추가
    public void like(String authorization, Long postId) {
        Long userId = authService.extractUserId(authorization);

        if (repo.hasLiked(postId, userId)) {
            throw new BusinessException(ErrorCode.LIKE_ALREADY_EXISTS);
        }
        repo.addLike(postId, userId);
    }

    // 좋아요 취소
    public void unlike(String authorization, Long postId) {
        Long userId = authService.extractUserId(authorization);

        if (!repo.hasLiked(postId, userId)) {
            throw new BusinessException(ErrorCode.LIKE_NOT_FOUND);
        }
        repo.removeLike(postId, userId);
    }

    // 좋아요 개수 조회
    public int getLikeCount(Long postId) {
        return repo.getLikeCount(postId);
    }
}
