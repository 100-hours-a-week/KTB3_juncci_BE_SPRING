package com.example.WEEK04.controller;

import com.example.WEEK04.common.ResponseFactory;
import com.example.WEEK04.model.dto.request.CommentCreateRequest;
import com.example.WEEK04.model.dto.response.CommentListResponse;
import com.example.WEEK04.model.dto.response.CommentResponse;
import com.example.WEEK04.model.dto.response.ErrorResponse;
import com.example.WEEK04.model.entity.Comment;
import com.example.WEEK04.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(
            summary = "댓글 작성 API",
            description = """
                    특정 게시글에 댓글을 작성합니다.  
                    Authorization 헤더는 선택사항이며, 본문에는 댓글 내용이 필요합니다.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "댓글 작성 성공",
                    content = @Content(
                            schema = @Schema(example = """
                                    {
                                      "code": "created",
                                      "message": "리소스가 생성되었습니다.",
                                      "data": { "comment_id": 10 }
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
    public ResponseEntity<?> createComment(
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @PathVariable Long postId,
            @RequestBody CommentCreateRequest req
    ) {
        Comment comment = commentService.create(authorization, postId, req.getContent());
        return responseFactory.created(new IdData(comment.getId()));
    }

    @Operation(
            summary = "댓글 삭제 API",
            description = "특정 게시글의 댓글을 삭제합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "댓글 삭제 성공"),
            @ApiResponse(
                    responseCode = "404",
                    description = "댓글을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @PathVariable Long postId,
            @PathVariable Long commentId
    ) {
        commentService.delete(authorization, postId, commentId);
        return responseFactory.noContent();
    }

    @Operation(
            summary = "댓글 목록 조회 API",
            description = "게시글 ID로 모든 댓글 목록을 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "댓글 목록 조회 성공",
                    content = @Content(
                            schema = @Schema(implementation = CommentListResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": "ok",
                                              "message": "요청이 성공했습니다.",
                                              "data": {
                                                "comments": [
                                                  { "id": 1, "authorId": 2, "content": "좋은 글이네요!", "createdAt": "2025-10-19T10:00:00" },
                                                  { "id": 2, "authorId": 3, "content": "공감합니다.", "createdAt": "2025-10-19T10:05:00" }
                                                ]
                                              }
                                            }
                                            """
                            )
                    )
            )
    })
    @GetMapping
    public ResponseEntity<?> getComments(@PathVariable Long postId) {
        List<Comment> comments = commentService.getCommentsByPostId(postId);
        return responseFactory.ok(new CommentListResponse(comments));
    }

    @Operation(
            summary = "댓글 단건 조회 API",
            description = "댓글 ID로 특정 댓글의 상세 정보를 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "댓글 조회 성공",
                    content = @Content(schema = @Schema(implementation = CommentResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "댓글을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @GetMapping("/{commentId}")
    public ResponseEntity<?> getComment(@PathVariable Long postId, @PathVariable Long commentId) {
        Comment comment = commentService.getCommentById(postId, commentId);
        return responseFactory.ok(new CommentResponse(comment));
    }

    record IdData(Long comment_id) {}
}
