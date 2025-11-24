package com.example.WEEK04.controller;

import com.example.WEEK04.common.ApiResponseDto;
import com.example.WEEK04.common.ResponseFactory;
import com.example.WEEK04.model.dto.response.LikeActionResponse;
import com.example.WEEK04.model.dto.response.LikeCountResponse;
import com.example.WEEK04.service.LikeService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts/{postId}/likes")
public class LikeController {

    private final LikeService likeService;
    private final ResponseFactory responseFactory;

    public LikeController(LikeService likeService, ResponseFactory responseFactory) {
        this.likeService = likeService;
        this.responseFactory = responseFactory;
    }

    /** 좋아요 추가 */
    @Operation(summary = "좋아요 추가 API")
    @PostMapping
    public ResponseEntity<ApiResponseDto<LikeActionResponse>> like(
            @PathVariable Long postId
    ) {
        LikeActionResponse response = likeService.like(postId);
        return responseFactory.ok(response);
    }

    /** 좋아요 취소 */
    @Operation(summary = "좋아요 취소 API")
    @DeleteMapping
    public ResponseEntity<ApiResponseDto<LikeActionResponse>> unlike(
            @PathVariable Long postId
    ) {
        LikeActionResponse response = likeService.unlike(postId);
        return responseFactory.ok(response);
    }

    /** 좋아요 개수 조회 */
    @Operation(summary = "좋아요 개수 조회 API")
    @GetMapping
    public ResponseEntity<ApiResponseDto<LikeCountResponse>> getLikeCount(
            @PathVariable Long postId
    ) {
        LikeCountResponse response = likeService.getLikeCount(postId);
        return responseFactory.ok(response);
    }
}
