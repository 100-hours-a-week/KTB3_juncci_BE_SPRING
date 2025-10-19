package com.example.WEEK04.controller;

import com.example.WEEK04.common.ResponseFactory;
import com.example.WEEK04.exception.BusinessException;
import com.example.WEEK04.exception.ErrorCode;
import com.example.WEEK04.model.dto.request.LoginRequest;
import com.example.WEEK04.model.dto.request.SignupRequest;
import com.example.WEEK04.model.dto.response.ErrorResponse;
import com.example.WEEK04.model.entity.User;
import com.example.WEEK04.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final ResponseFactory responseFactory;

    public UserController(UserService userService, ResponseFactory responseFactory) {
        this.userService = userService;
        this.responseFactory = responseFactory;
    }

    // 회원가입
    @Operation(
            summary = "회원가입 API",
            description = """
                    새로운 사용자를 등록합니다.  
                    이메일, 이름, 비밀번호를 포함한 요청 본문을 전달해야 합니다.  
                    이메일 중복 시 409 에러를 반환합니다.
                    """,
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "회원가입 요청 본문",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = SignupRequest.class),
                            examples = @ExampleObject(
                                    name = "예시 요청",
                                    value = """
                                            {
                                              "email": "user@example.com",
                                              "password": "1234abcd",
                                              "name": "박준서"
                                            }
                                            """
                            )
                    )
            )
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "회원가입 성공",
                    content = @Content(
                            schema = @Schema(implementation = SignupResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "user_id": 1,
                                              "access_token": "ACCESS-TOKEN-1"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "이메일 중복",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": "EMAIL_DUPLICATE",
                                              "message": "이미 사용 중인 이메일입니다."
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 내부 오류",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @PostMapping
    public ResponseEntity<?> register(@RequestBody SignupRequest req) {
        User user = userService.signup(req);
        return responseFactory.created(new SignupResponse(user.getId(), null));
    }

    // 로그인
    @Operation(
            summary = "로그인 API",
            description = """
                    등록된 사용자의 이메일과 비밀번호로 로그인합니다.  
                    유효한 사용자일 경우 Access Token을 반환합니다.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "로그인 성공",
                    content = @Content(
                            schema = @Schema(implementation = SignupResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "user_id": 1,
                                              "access_token": "ACCESS-TOKEN-1"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "인증 실패 (비밀번호 불일치 또는 존재하지 않는 사용자)",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": "AUTH-INVALID",
                                              "message": "이메일 또는 비밀번호가 올바르지 않습니다."
                                            }
                                            """
                            )
                    )
            )
    })
    @PostMapping("/auth")
    public ResponseEntity<?> auth(@RequestBody LoginRequest req) {
        User user = userService.login(req);
        String dummyAccessToken = "ACCESS-TOKEN-" + user.getId();
        return responseFactory.ok(new SignupResponse(user.getId(), dummyAccessToken));
    }

    // 이메일 중복 확인
    @Operation(
            summary = "이메일 중복 확인 API",
            description = """
                    이메일의 중복 여부를 확인합니다.  
                    true: 사용 가능 / false: 이미 존재하는 이메일
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "이메일 사용 가능",
                    content = @Content(
                            schema = @Schema(implementation = CheckEmailResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            { "available": true }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "이메일 중복됨",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": "EMAIL_DUPLICATE",
                                              "message": "이미 사용 중인 이메일입니다."
                                            }
                                            """
                            )
                    )
            )
    })
    @GetMapping("/check-email")
    public ResponseEntity<?> checkEmail(@RequestParam String email) {
        boolean available = userService.isEmailAvailable(email);
        if (!available) {
            throw new BusinessException(ErrorCode.EMAIL_DUPLICATE);
        }
        return responseFactory.ok(new CheckEmailResponse(true));
    }

    // 내부 DTO
    record SignupResponse(Long user_id, String access_token) {}
    record CheckEmailResponse(boolean available) {}
}
