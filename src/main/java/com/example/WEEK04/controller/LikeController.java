package com.example.WEEK04.controller;

import com.example.WEEK04.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts/{postId}/likes")
public class LikeController {

    private final PostService.LikeService likeService;
    public LikeController(PostService.LikeService likeService) {
        this.likeService = likeService;
    }

    // 좋아요 누르기
    @PostMapping
    public ResponseEntity<?> like(
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @PathVariable Long postId
    ) {
        likeService.like(authorization, postId);
        return ResponseEntity.ok(new Response("like_added", new Data(postId), null));
    }

    // 좋아요 취소
    @DeleteMapping
    public ResponseEntity<?> unlike(
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @PathVariable Long postId
    ) {
        likeService.unlike(authorization, postId);
        return ResponseEntity.ok(new Response("like_removed", new Data(postId), null));
    }

    // 좋아요 개수 조회
    @GetMapping
    public ResponseEntity<?> getLikeCount(@PathVariable Long postId) {
        int count = likeService.getLikeCount(postId);
        return ResponseEntity.ok(new Response("ok", new LikeCount(count), null));
    }

    record Response(String message, Object data, Object error) {}
    record Data(Long post_id) {}
    record LikeCount(int like_count) {}
}
