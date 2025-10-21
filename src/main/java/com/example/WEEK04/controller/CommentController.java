package com.example.WEEK04.controller;

import com.example.WEEK04.common.ApiResponseDto;
import com.example.WEEK04.common.ResponseFactory;
import com.example.WEEK04.model.dto.request.CommentCreateRequest;
import com.example.WEEK04.model.entity.Comment;
import com.example.WEEK04.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts/{postId}/comments")
public class CommentController {

    private final CommentService commentService;
    private final ResponseFactory responseFactory;

    public CommentController(CommentService commentService, ResponseFactory responseFactory) {
        this.commentService = commentService;
        this.responseFactory = responseFactory;
    }

    @Operation(summary = "댓글 작성 API", description = "특정 게시글에 댓글을 작성합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "댓글 작성 성공",
                    content = @Content(schema = @Schema(implementation = ApiResponseDto.class),
                            examples = @ExampleObject(value = """
                            {
                              "code": "created",
                              "message": "리소스가 생성되었습니다.",
                              "data": { "comment_id": 10 }
                            }
                            """))
            ),
            @ApiResponse(responseCode = "400", description = "요청 본문 누락 또는 잘못된 형식"),
            @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없음")
    })
    @PostMapping
    public ResponseEntity<ApiResponseDto<IdData>> createComment(
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @PathVariable Long postId,
            @Valid @RequestBody CommentCreateRequest req
    ) {
        Comment comment = commentService.create(authorization, postId, req.getContent());
        return responseFactory.created(new IdData(comment.getId()));
    }

    @Operation(summary = "댓글 삭제 API", description = "특정 게시글의 댓글을 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "댓글 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "댓글을 찾을 수 없음")
    })
    @DeleteMapping("/{commentId}")
    public ResponseEntity<ApiResponseDto<Void>> deleteComment(
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @PathVariable Long postId,
            @PathVariable Long commentId
    ) {
        commentService.delete(authorization, postId, commentId);
        return responseFactory.noContent();
    }

    @Operation(summary = "댓글 목록 조회 API", description = "게시글 ID로 모든 댓글 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<ApiResponseDto<List<Comment>>> getComments(@PathVariable Long postId) {
        List<Comment> comments = commentService.getCommentsByPostId(postId);
        return responseFactory.ok(comments);
    }

    @Operation(summary = "댓글 단건 조회 API", description = "댓글 ID로 특정 댓글의 상세 정보를 조회합니다.")
    @GetMapping("/{commentId}")
    public ResponseEntity<ApiResponseDto<Comment>> getComment(
            @PathVariable Long postId,
            @PathVariable Long commentId
    ) {
        Comment comment = commentService.getCommentById(postId, commentId);
        return responseFactory.ok(comment);
    }

    record IdData(Long comment_id) {}
}
