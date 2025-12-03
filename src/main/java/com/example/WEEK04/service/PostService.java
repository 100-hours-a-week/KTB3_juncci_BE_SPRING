package com.example.WEEK04.service;

import com.example.WEEK04.exception.BusinessException;
import com.example.WEEK04.exception.ErrorCode;
import com.example.WEEK04.model.dto.PostSummaryDto;
import com.example.WEEK04.model.dto.request.PostCreateRequest;
import com.example.WEEK04.model.dto.request.PostUpdateRequest;
import com.example.WEEK04.model.dto.response.PostDetailResponse;
import com.example.WEEK04.model.dto.response.PostListResponse;
import com.example.WEEK04.model.entity.Post;
import com.example.WEEK04.model.entity.User;
import com.example.WEEK04.repository.LikeRepository;
import com.example.WEEK04.repository.PostRepository;
import com.example.WEEK04.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepo;
    private final UserRepository userRepo;
    private final LikeRepository likeRepo;

    /** 현재 로그인 유저 반환 */
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

    /** 게시글 작성 */
    public Long create(PostCreateRequest req) {
        User user = getCurrentUser();

        String imageString = (req.getImages() != null && !req.getImages().isEmpty())
                ? String.join(",", req.getImages())
                : "";

        Post post = new Post(user, req.getTitle(), req.getContent(), imageString);
        user.addPost(post);

        return postRepo.save(post).getId();
    }

    /** 게시글 수정 */
    public Long update(Long postId, PostUpdateRequest req) {
        User user = getCurrentUser();

        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));

        if (!post.getUser().getId().equals(user.getId())) {
            throw new BusinessException(ErrorCode.AUTH_FORBIDDEN);
        }

        String images = (req.getImages() != null && !req.getImages().isEmpty())
                ? String.join(",", req.getImages())
                : "";

        post.updateTitle(req.getTitle());
        post.updateContent(req.getContent());
        post.updateImages(images);

        return post.getId();
    }

    /** 게시글 삭제 */
    public void delete(Long postId) {
        User user = getCurrentUser();

        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));

        if (!post.getUser().getId().equals(user.getId())) {
            throw new BusinessException(ErrorCode.AUTH_FORBIDDEN);
        }

        postRepo.delete(post);
    }

    /** 🔥 게시글 리스트 조회 (N+1 제거 + Projection) */
    @Transactional
    public PostListResponse getPosts(Integer page, Integer size, String sort) {

        Sort s = "asc".equalsIgnoreCase(sort)
                ? Sort.by("id").ascending()
                : Sort.by("id").descending();

        Pageable pageable = PageRequest.of(page - 1, size, s);

        // 🔥 Projection 기반 (단 한 번의 쿼리)
        Page<PostSummaryDto> pageObj = postRepo.findPostSummaries(pageable);

        List<PostListResponse.PostSummary> summaries = pageObj.getContent().stream()
                .map(dto -> new PostListResponse.PostSummary(
                        dto.postId(),
                        dto.title(),
                        dto.createdAt(),
                        dto.commentCount(),
                        dto.likeCount(),
                        dto.viewCount(),
                        dto.authorId(),
                        dto.authorNickname()
                ))
                .toList();

        return new PostListResponse(
                "ok",
                new PostListResponse.Data(
                        summaries,
                        page,
                        size,
                        (int) pageObj.getTotalElements()
                ),
                null
        );
    }

    /** 게시글 상세 조회 */
    @Transactional
    public PostDetailResponse getPostById(Long id) {

        User user = null;
        boolean isLiked = false;

        try {
            user = getCurrentUser();
            isLiked = likeRepo.existsByPostIdAndUserId(id, user.getId());
        } catch (Exception ignored) {}

        Post post = postRepo.findPostWithDetails(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));

        post.incrementViewCount();
        int likeCount = likeRepo.countByPostId(id);

        return new PostDetailResponse(
                "ok",
                new PostDetailResponse.Data(post, likeCount, post.getViewCount(), isLiked),
                null
        );
    }

    /** 🔥 내가 작성한 글 목록 (Projection 버전) */
    @Transactional
    public PostListResponse getMyPosts(Integer page, Integer size, String sort) {

        User currentUser = getCurrentUser();

        Sort s = "asc".equalsIgnoreCase(sort)
                ? Sort.by("id").ascending()
                : Sort.by("id").descending();

        Pageable pageable = PageRequest.of(page - 1, size, s);

        // 🔥 동일 사용자만 조회하므로 Projection 재사용
        Page<PostSummaryDto> pageObj =
                postRepo.findPostSummariesByUserId(currentUser.getId(), pageable);

        List<PostListResponse.PostSummary> summaries = pageObj.getContent().stream()
                .map(dto -> new PostListResponse.PostSummary(
                        dto.postId(),
                        dto.title(),
                        dto.createdAt(),
                        dto.commentCount(),
                        dto.likeCount(),
                        dto.viewCount(),
                        dto.authorId(),
                        dto.authorNickname()
                ))
                .toList();

        return new PostListResponse(
                "ok",
                new PostListResponse.Data(
                        summaries,
                        page,
                        size,
                        (int) pageObj.getTotalElements()
                ),
                null
        );
    }
}
