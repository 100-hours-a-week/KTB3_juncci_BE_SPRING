package com.example.WEEK04.controller;

import com.example.WEEK04.common.ResponseFactory;
import com.example.WEEK04.model.dto.response.ErrorResponse;
import com.example.WEEK04.service.PostService;
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

    private final PostService.LikeService likeService;
    private final ResponseFactory responseFactory;

    public LikeController(PostService.LikeService likeService, ResponseFactory responseFactory) {
        this.likeService = likeService;
        this.responseFactory = responseFactory;
    }

    @Operation(
            summary = "좋아요 추가 API",
            description = """
                    특정 게시글에 좋아요를 추가합니다.  
                    Authorization 헤더는 선택사항입니다.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "좋아요 성공",
                    content = @Content(
                            schema = @Schema(example = """
                                    {
                                      "code": "ok",
                                      "message": "요청이 성공했습니다.",
                                      "data": { "post_id": 1 }
                                    }
                                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "게시글을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @PostMapping
    public ResponseEntity<?> like(
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @PathVariable Long postId
    ) {
        likeService.like(authorization, postId);
        return responseFactory.ok(new IdData(postId));
    }

    @Operation(
            summary = "좋아요 취소 API",
            description = """
                    특정 게시글에 추가된 좋아요를 취소합니다.  
                    Authorization 헤더는 선택사항입니다.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "좋아요 취소 성공",
                    content = @Content(
                            schema = @Schema(example = """
                                    {
                                      "code": "ok",
                                      "message": "요청이 성공했습니다.",
                                      "data": { "post_id": 1 }
                                    }
                                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "게시글 또는 좋아요 기록을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @DeleteMapping
    public ResponseEntity<?> unlike(
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @PathVariable Long postId
    ) {
        likeService.unlike(authorization, postId);
        return responseFactory.ok(new IdData(postId));
    }

    @Operation(
            summary = "좋아요 개수 조회 API",
            description = "특정 게시글의 좋아요 개수를 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "좋아요 개수 조회 성공",
                    content = @Content(
                            schema = @Schema(example = """
                                    {
                                      "code": "ok",
                                      "message": "요청이 성공했습니다.",
                                      "data": { "like_count": 5 }
                                    }
                                    """)
                    )
            )
    })
    @GetMapping
    public ResponseEntity<?> getLikeCount(@PathVariable Long postId) {
        int count = likeService.getLikeCount(postId);
        return responseFactory.ok(new LikeCountData(count));
    }

    record IdData(Long post_id) {}
    record LikeCountData(int like_count) {}
}
