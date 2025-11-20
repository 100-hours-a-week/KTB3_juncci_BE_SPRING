package com.example.WEEK04.service;

import com.example.WEEK04.exception.BusinessException;
import com.example.WEEK04.exception.ErrorCode;
import com.example.WEEK04.model.dto.request.PostCreateRequest;
import com.example.WEEK04.model.dto.request.PostUpdateRequest;
import com.example.WEEK04.model.dto.response.PostDetailResponse;
import com.example.WEEK04.model.dto.response.PostListResponse;
import com.example.WEEK04.model.entity.Post;
import com.example.WEEK04.model.entity.User;
import com.example.WEEK04.repository.LikeRepository;
import com.example.WEEK04.repository.PostRepository;
import com.example.WEEK04.repository.UserRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PostService {

    private final PostRepository postRepo;
    private final UserRepository userRepo;
    private final LikeRepository likeRepo;
    private final AuthService authService;

    public PostService(PostRepository postRepo, UserRepository userRepo,
                       LikeRepository likeRepo, AuthService authService) {
        this.postRepo = postRepo;
        this.userRepo = userRepo;
        this.likeRepo = likeRepo;
        this.authService = authService;
    }

    /** 게시글 작성 */
    public Long create(String authorization, PostCreateRequest req) {
        Long userId = authService.extractUserId(authorization);
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        String imageString = (req.getImages() != null && !req.getImages().isEmpty())
                ? String.join(",", req.getImages())
                : "";

        Post post = new Post(user, req.getTitle(), req.getContent(), imageString);
        user.addPost(post);

        return postRepo.save(post).getId();
    }

    /** 게시글 수정 추가 */
    /** 게시글 수정 추가 */
    public Long update(String authorization, Long postId, PostUpdateRequest req) {
        Long userId = authService.extractUserId(authorization);

        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));

        // 권한 확인
        if (!post.getUser().getId().equals(userId)) {
            throw new BusinessException(ErrorCode.AUTH_FORBIDDEN);
        }

        String imageString = (req.getImages() != null && !req.getImages().isEmpty())
                ? String.join(",", req.getImages())
                : "";

        // 필드 업데이트
        post.updateTitle(req.getTitle());
        post.updateContent(req.getContent());
        post.updateImages(imageString);

        return post.getId();
    }


    /** 게시글 목록 조회 (작성자 포함) */
    @Transactional(readOnly = true)
    public PostListResponse getPosts(Integer page, Integer size, String sort) {
        Sort s = "asc".equalsIgnoreCase(sort)
                ? Sort.by("id").ascending()
                : Sort.by("id").descending();

        Pageable pageable = PageRequest.of(page - 1, size, s);
        List<Post> posts = postRepo.findAllWithUser();
        Page<Post> pageObj = new PageImpl<>(posts, pageable, posts.size());

        List<PostListResponse.PostSummary> summaries = pageObj.getContent().stream()
                .map(p -> new PostListResponse.PostSummary(
                        p,
                        likeRepo.countByPostId(p.getId()),
                        p.getViewCount()
                )).toList();

        return new PostListResponse(
                "ok",
                new PostListResponse.Data(summaries, page, size, (int) pageObj.getTotalElements()),
                null
        );
    }

    /** 게시글 상세 조회 */
    @Transactional
    public PostDetailResponse getPostById(Long id, String authorization) {

        Long userId = null;
        boolean isLiked = false;

        if (authorization != null && authorization.startsWith("Bearer ACCESS-TOKEN-")) {
            try {
                userId = authService.extractUserId(authorization);
                isLiked = likeRepo.existsByPostIdAndUserId(id, userId);
            } catch (Exception ignored) {}
        }

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


    /** 게시글 삭제 추가 */
    public void delete(String authorization, Long postId) {
        Long userId = authService.extractUserId(authorization);

        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));

        if (!post.getUser().getId().equals(userId)) {
            throw new BusinessException(ErrorCode.AUTH_FORBIDDEN);
        }

        postRepo.delete(post);
    }
}
