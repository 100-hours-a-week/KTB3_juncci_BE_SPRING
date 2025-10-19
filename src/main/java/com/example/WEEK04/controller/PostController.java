package com.example.WEEK04.controller;

import com.example.WEEK04.common.ResponseFactory;
import com.example.WEEK04.model.dto.request.PostCreateRequest;
import com.example.WEEK04.model.dto.request.PostUpdateRequest;
import com.example.WEEK04.model.dto.response.ErrorResponse;
import com.example.WEEK04.model.dto.response.PostDetailResponse;
import com.example.WEEK04.model.dto.response.PostListResponse;
import com.example.WEEK04.model.entity.Post;
import com.example.WEEK04.service.PostService;
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
@RequestMapping("/posts")
public class PostController {

    private static final int DEFAULT_PAGE = 1;
    private static final int DEFAULT_SIZE = 10;
    private static final String DEFAULT_SORT = "desc";

    private final PostService postService;
    private final ResponseFactory responseFactory;

    public PostController(PostService postService, ResponseFactory responseFactory) {
        this.postService = postService;
        this.responseFactory = responseFactory;
    }

    // 게시글 작성
    @Operation(
            summary = "게시글 작성 API",
            description = """
                    새로운 게시글을 작성합니다.  
                    제목, 내용, 이미지 리스트를 포함한 요청 본문을 전달해야 합니다.  
                    Authorization 헤더는 선택사항입니다.
                    """,
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "게시글 작성 요청 본문",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = PostCreateRequest.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "title": "오늘의 일상",
                                              "content": "점심에 카레를 먹었어요.",
                                              "images": ["https://cdn.example.com/img1.png", "https://cdn.example.com/img2.png"]
                                            }
                                            """
                            )
                    )
            )
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "게시글 작성 성공",
                    content = @Content(
                            schema = @Schema(example = """
                                    {
                                      "code": "created",
                                      "message": "리소스가 생성되었습니다.",
                                      "data": { "post_id": 1 }
                                    }
                                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 내부 오류",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @PostMapping
    public ResponseEntity<?> create(
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @RequestBody PostCreateRequest req
    ) {
        Post post = postService.create(authorization, req);
        return responseFactory.created(new IdData(post.getId()));
    }

    // 게시글 수정
    @Operation(
            summary = "게시글 수정 API",
            description = """
                    기존 게시글의 제목, 내용 또는 이미지를 수정합니다.  
                    Authorization 헤더는 선택사항입니다.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "게시글 수정 성공",
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
    @PatchMapping("/{postId}")
    public ResponseEntity<?> update(
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @PathVariable Long postId,
            @RequestBody PostUpdateRequest req
    ) {
        Post updated = postService.update(authorization, postId, req);
        return responseFactory.ok(new IdData(updated.getId()));
    }

    // 게시글 삭제
    @Operation(
            summary = "게시글 삭제 API",
            description = "게시글 ID로 특정 게시글을 삭제합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "게시글 삭제 성공"),
            @ApiResponse(
                    responseCode = "404",
                    description = "게시글을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @DeleteMapping("/{postId}")
    public ResponseEntity<?> delete(
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @PathVariable Long postId
    ) {
        postService.delete(authorization, postId);
        return responseFactory.noContent();
    }

    // 게시글 목록 조회
    @Operation(
            summary = "게시글 목록 조회 API",
            description = """
                    전체 게시글 목록을 조회합니다.  
                    기본 정렬: 최신순(desc), 기본 페이지: 1, 기본 크기: 10
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "게시글 목록 조회 성공",
                    content = @Content(
                            schema = @Schema(implementation = PostListResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": "ok",
                                              "message": "요청이 성공했습니다.",
                                              "data": {
                                                "posts": [
                                                  { "id": 1, "title": "첫 번째 글", "authorId": 3, "likeCount": 10, "commentCount": 2 },
                                                  { "id": 2, "title": "두 번째 글", "authorId": 4, "likeCount": 5, "commentCount": 1 }
                                                ],
                                                "page": 1,
                                                "size": 10,
                                                "total": 2
                                              }
                                            }
                                            """
                            )
                    )
            )
    })
    @GetMapping
    public ResponseEntity<?> getPosts(
            @RequestParam(defaultValue = "" + DEFAULT_PAGE) Integer page,
            @RequestParam(defaultValue = "" + DEFAULT_SIZE) Integer size,
            @RequestParam(defaultValue = DEFAULT_SORT) String sort
    ) {
        List<Post> posts = postService.getPosts(page, size, sort);
        int total = postService.getTotalCount();
        return responseFactory.ok(new PostListResponse.Data(posts, page, size, total));
    }

    // 게시글 단건 조회
    @Operation(
            summary = "게시글 단건 조회 API",
            description = "게시글 ID로 게시글의 상세 정보를 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "게시글 조회 성공",
                    content = @Content(schema = @Schema(implementation = PostDetailResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "게시글을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @GetMapping("/{postId}")
    public ResponseEntity<?> getPostById(@PathVariable Long postId) {
        Post post = postService.getPostById(postId);
        return responseFactory.ok(new PostDetailResponse.Data(post));
    }

    // 내부 record
    record IdData(Long post_id) {}
}
