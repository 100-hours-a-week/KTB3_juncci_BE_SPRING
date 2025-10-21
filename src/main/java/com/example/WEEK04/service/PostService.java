package com.example.WEEK04.service;

import com.example.WEEK04.exception.BusinessException;
import com.example.WEEK04.exception.ErrorCode;
import com.example.WEEK04.model.entity.Post;
import com.example.WEEK04.model.dto.request.PostCreateRequest;
import com.example.WEEK04.model.dto.request.PostUpdateRequest;
import com.example.WEEK04.repository.DummyLikeRepository;
import com.example.WEEK04.repository.DummyPostRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PostService {

    private final DummyPostRepository postRepo;
    private final DummyLikeRepository likeRepo;
    private final AuthService authService;

    public PostService(DummyPostRepository postRepo, DummyLikeRepository likeRepo, AuthService authService) {
        this.postRepo = postRepo;
        this.likeRepo = likeRepo;
        this.authService = authService;
    }

    // 게시글 작성
    public Post create(String authorization, PostCreateRequest req) {
        Long userId = authService.extractUserId(authorization);
        Post newPost = new Post(null, userId, req.getTitle(), req.getContent(), req.getImages());
        return postRepo.save(newPost);
    }

    // 게시글 수정
    public Post update(String authorization, Long postId, PostUpdateRequest req) {
        Long userId = authService.extractUserId(authorization);

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
        Long userId = authService.extractUserId(authorization);

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
        posts.forEach(p -> p.setLikeCount(likeRepo.getLikeCount(p.getId())));
        return posts;
    }

    // 게시글 단일 조회
    public Post getPostById(Long id) {
        Post post = postRepo.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));

        post.setLikeCount(likeRepo.getLikeCount(id));
        return post;
    }

    public int getTotalCount() {
        return postRepo.count();
    }
}
