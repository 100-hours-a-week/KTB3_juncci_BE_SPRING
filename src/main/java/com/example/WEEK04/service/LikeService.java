package com.example.WEEK04.service;

import com.example.WEEK04.exception.BusinessException;
import com.example.WEEK04.exception.ErrorCode;
import com.example.WEEK04.model.dto.response.LikeActionResponse;
import com.example.WEEK04.model.dto.response.LikeCountResponse;
import com.example.WEEK04.model.entity.Like;
import com.example.WEEK04.model.entity.Post;
import com.example.WEEK04.model.entity.User;
import com.example.WEEK04.repository.LikeRepository;
import com.example.WEEK04.repository.PostRepository;
import com.example.WEEK04.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepo;
    private final PostRepository postRepo;
    private final UserRepository userRepo;

    /** 현재 로그인 사용자 */
    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        return userRepo.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
    }

    /** 좋아요 추가 */
    public LikeActionResponse like(Long postId) {
        User user = getCurrentUser();

        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));

        if (likeRepo.existsByPostIdAndUserId(postId, user.getId())) {
            throw new BusinessException(ErrorCode.LIKE_ALREADY_EXISTS);
        }

        Like like = new Like(post, user);
        post.addLike(like);
        user.addLike(like);

        likeRepo.save(like);

        return new LikeActionResponse(postId, "좋아요 추가 성공");
    }

    /** 좋아요 취소 */
    public LikeActionResponse unlike(Long postId) {
        User user = getCurrentUser();

        Like like = likeRepo.findByPostIdAndUserId(postId, user.getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.LIKE_NOT_FOUND));

        Post post = like.getPost();

        post.removeLike(like);
        user.removeLike(like);

        likeRepo.delete(like);

        return new LikeActionResponse(postId, "좋아요 취소 성공");
    }

    /** 좋아요 개수 조회 */
    @Transactional
    public LikeCountResponse getLikeCount(Long postId) {
        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));
        int count = likeRepo.countByPostId(postId);
        return new LikeCountResponse(postId, count);
    }
}
