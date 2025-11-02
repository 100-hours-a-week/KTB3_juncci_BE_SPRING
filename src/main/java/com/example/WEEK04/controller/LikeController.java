package com.example.WEEK04.controller;

import com.example.WEEK04.common.ApiResponseDto;
import com.example.WEEK04.common.ResponseFactory;
import com.example.WEEK04.model.dto.response.LikeActionResponse;
import com.example.WEEK04.model.dto.response.LikeCountResponse;
import com.example.WEEK04.service.LikeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    /** 좋아요 추가 API */
    @Operation(summary = "좋아요 추가 API", description = "특정 게시글에 좋아요를 추가합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "좋아요 성공",
                    content = @Content(schema = @Schema(implementation = ApiResponseDto.class),
                            examples = @ExampleObject(value = """
                            {
                              "code": "ok",
                              "message": "요청이 성공했습니다.",
                              "data": { "post_id": 1, "message": "좋아요 추가 성공" }
                            }
                            """))
            ),
            @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없음")
    })
    @PostMapping
    public ResponseEntity<ApiResponseDto<LikeActionResponse>> like(
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @PathVariable Long postId
    ) {
        LikeActionResponse response = likeService.like(authorization, postId);
        return responseFactory.ok(response);
    }

    /** 좋아요 취소 API */
    @Operation(summary = "좋아요 취소 API", description = "특정 게시글의 좋아요를 취소합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "좋아요 취소 성공"),
            @ApiResponse(responseCode = "404", description = "좋아요 기록을 찾을 수 없음")
    })
    @DeleteMapping
    public ResponseEntity<ApiResponseDto<LikeActionResponse>> unlike(
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @PathVariable Long postId
    ) {
        LikeActionResponse response = likeService.unlike(authorization, postId);
        return responseFactory.ok(response);
    }

    /** 좋아요 개수 조회 API */
    @Operation(summary = "좋아요 개수 조회 API", description = "특정 게시글의 좋아요 개수를 조회합니다.")
    @GetMapping
    public ResponseEntity<ApiResponseDto<LikeCountResponse>> getLikeCount(@PathVariable Long postId) {
        LikeCountResponse response = likeService.getLikeCount(postId);
        return responseFactory.ok(response);
    }
}
