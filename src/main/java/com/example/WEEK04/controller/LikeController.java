package com.example.WEEK04.controller;

import com.example.WEEK04.common.ResponseFactory;
import com.example.WEEK04.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts/{postId}/likes")
public class LikeController {

    private final PostService.LikeService likeService;
    private final ResponseFactory responseFactory; // ✅ 추가

    public LikeController(PostService.LikeService likeService, ResponseFactory responseFactory) {
        this.likeService = likeService;
        this.responseFactory = responseFactory;
    }

    // ✅ 좋아요 누르기
    @PostMapping
    public ResponseEntity<?> like(
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @PathVariable Long postId
    ) {
        likeService.like(authorization, postId);
        return responseFactory.ok(new IdData(postId));
    }

    // ✅ 좋아요 취소
    @DeleteMapping
    public ResponseEntity<?> unlike(
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @PathVariable Long postId
    ) {
        likeService.unlike(authorization, postId);
        return responseFactory.ok(new IdData(postId));
    }

    // ✅ 좋아요 개수 조회
    @GetMapping
    public ResponseEntity<?> getLikeCount(@PathVariable Long postId) {
        int count = likeService.getLikeCount(postId);
        return responseFactory.ok(new LikeCountData(count));
    }

    // ✅ 내부 record DTO
    record IdData(Long post_id) {}
    record LikeCountData(int like_count) {}
}
