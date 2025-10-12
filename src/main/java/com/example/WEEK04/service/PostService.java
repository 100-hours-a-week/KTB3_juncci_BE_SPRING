package com.example.WEEK04.service;

import com.example.WEEK04.exception.BusinessException;
import com.example.WEEK04.exception.ErrorCode;
import com.example.WEEK04.model.entity.Post;
import com.example.WEEK04.model.dto.request.PostCreateRequest;
import com.example.WEEK04.model.dto.request.PostUpdateRequest;
import com.example.WEEK04.repository.DummyLikeRepository;
import com.example.WEEK04.repository.DummyPostRepository;
import com.example.WEEK04.validation.PostValidator;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {

    private final DummyPostRepository postRepo;
    private final DummyLikeRepository likeRepo;
    private final PostValidator validator;

    public PostService(DummyPostRepository postRepo, DummyLikeRepository likeRepo, PostValidator validator) {
        this.postRepo = postRepo;
        this.likeRepo = likeRepo;
        this.validator = validator;
    }

    // 게시글 작성
    public Post create(String authorization, PostCreateRequest req) {
        Long userId = extractUser(authorization);

        validator.validateCreate(req);
        Post newPost = new Post(null, userId, req.getTitle(), req.getContent(), req.getImages());
        return postRepo.save(newPost);
    }

    // 게시글 수정
    public Post update(String authorization, Long postId, PostUpdateRequest req) {
        Long userId = extractUser(authorization);
        validator.validateUpdate(req);

        Post existing = postRepo.findById(postId)
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));

        if (!existing.getAuthorId().equals(userId)) {
            throw new BusinessException(ErrorCode.AUTH_FORBIDDEN);
        }

        Post updated = new Post(existing.getId(), userId, req.getTitle(), req.getContent(), req.getImages());
        return postRepo.save(updated);
    }

    // 게시글 삭제
    public void delete(String authorization, Long postId) {
        Long userId = extractUser(authorization);

        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));

        if (!post.getAuthorId().equals(userId)) {
            throw new BusinessException(ErrorCode.AUTH_FORBIDDEN);
        }

        boolean deleted = postRepo.deleteById(postId);
        if (!deleted) throw new BusinessException(ErrorCode.POST_NOT_FOUND);
    }

    // 게시글 목록 조회
    public List<Post> getPosts(Integer page, Integer size, String sort) {
        if (page == null || page < 1 || size == null || size < 1) {
            throw new BusinessException(ErrorCode.POSTS_PARAM_INVALID);
        }

        List<Post> posts = postRepo.findAll(page, size, sort);

        // 각 게시글에 좋아요 개수 실시간 반영
        posts.forEach(p -> p.setLikeCount(likeRepo.getLikeCount(p.getId())));

        return posts;
    }

    // 게시글 단일 조회
    public Post getPostById(Long id) {
        Post post = postRepo.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));

        // 좋아요 수 동기화
        post.setLikeCount(likeRepo.getLikeCount(id));
        return post;
    }

    public int getTotalCount() {
        return postRepo.count();
    }

    // 내부 공통 토큰 파싱 로직
    private Long extractUser(String authorization) {
        if (authorization == null || !authorization.startsWith("Bearer ACCESS-TOKEN-")) {
            throw new BusinessException(ErrorCode.AUTH_TOKEN_INVALID);
        }
        try {
            return Long.parseLong(authorization.replace("Bearer ACCESS-TOKEN-", ""));
        } catch (NumberFormatException e) {
            throw new BusinessException(ErrorCode.AUTH_TOKEN_INVALID);
        }
    }

    @Service
    public static class LikeService {

        private final DummyLikeRepository repo;

        public LikeService(DummyLikeRepository repo) {
            this.repo = repo;
        }

        // 좋아요 추가
        public void like(String authorization, Long postId) {
            Long userId = extractUser(authorization);
            if (repo.hasLiked(postId, userId)) {
                throw new BusinessException(ErrorCode.LIKE_ALREADY_EXISTS);
            }
            repo.addLike(postId, userId);
        }

        // 좋아요 취소
        public void unlike(String authorization, Long postId) {
            Long userId = extractUser(authorization);
            if (!repo.hasLiked(postId, userId)) {
                throw new BusinessException(ErrorCode.LIKE_NOT_FOUND);
            }
            repo.removeLike(postId, userId);
        }

        // 좋아요 개수
        public int getLikeCount(Long postId) {
            return repo.getLikeCount(postId);
        }

        private Long extractUser(String authorization) {
            if (authorization == null || !authorization.startsWith("Bearer ACCESS-TOKEN-")) {
                throw new BusinessException(ErrorCode.AUTH_TOKEN_INVALID);
            }
            try {
                return Long.parseLong(authorization.replace("Bearer ACCESS-TOKEN-", ""));
            } catch (NumberFormatException e) {
                throw new BusinessException(ErrorCode.AUTH_TOKEN_INVALID);
            }
        }
    }
}
