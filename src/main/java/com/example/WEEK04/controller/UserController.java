package com.example.WEEK04.controller;

import com.example.WEEK04.common.ApiResponseDto;
import com.example.WEEK04.common.ResponseFactory;
import com.example.WEEK04.exception.BusinessException;
import com.example.WEEK04.exception.ErrorCode;
import com.example.WEEK04.model.dto.request.LoginRequest;
import com.example.WEEK04.model.dto.request.SignupRequest;
import com.example.WEEK04.model.entity.User;
import com.example.WEEK04.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
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

    @Operation(summary = "회원가입 API", description = "새로운 사용자를 등록합니다.")
    @PostMapping
    public ResponseEntity<ApiResponseDto<SignupData>> register(@Valid @RequestBody SignupRequest req) {
        User user = userService.signup(req);
        return responseFactory.created(new SignupData(user.getId(), null));
    }

    @Operation(summary = "로그인 API", description = "등록된 사용자의 이메일과 비밀번호로 로그인합니다.")
    @PostMapping("/auth")
    public ResponseEntity<ApiResponseDto<SignupData>> auth(@Valid @RequestBody LoginRequest req) {
        User user = userService.login(req);
        String dummyAccessToken = "ACCESS-TOKEN-" + user.getId();
        return responseFactory.ok(new SignupData(user.getId(), dummyAccessToken));
    }

    @Operation(summary = "이메일 중복 확인 API", description = "이메일의 중복 여부를 확인합니다.")
    @GetMapping("/check-email")
    public ResponseEntity<ApiResponseDto<CheckEmailData>> checkEmail(@RequestParam @Email String email) {
        boolean available = userService.isEmailAvailable(email);
        if (!available) {
            throw new BusinessException(ErrorCode.EMAIL_DUPLICATE);
        }
        return responseFactory.ok(new CheckEmailData(true));
    }

    record SignupData(Long user_id, String access_token) {}
    record CheckEmailData(boolean available) {}
}
