package com.example.WEEK04.controller;

import com.example.WEEK04.common.ApiResponseDto;
import com.example.WEEK04.common.ResponseFactory;
import com.example.WEEK04.exception.BusinessException;
import com.example.WEEK04.exception.ErrorCode;
import com.example.WEEK04.model.dto.request.*;
import com.example.WEEK04.model.dto.response.UserResponse;
import com.example.WEEK04.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
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

    /** 회원가입 */
    @Operation(summary = "회원가입 API", description = "새로운 사용자를 등록합니다.")
    @PostMapping
    public ResponseEntity<ApiResponseDto<SignupData>> register(@Valid @RequestBody SignupRequest req) {
        UserResponse user = userService.signup(req);
        return responseFactory.created(new SignupData(user.getId(), null));
    }

    /** 로그인 */
    @Operation(summary = "로그인 API", description = "등록된 사용자의 이메일과 비밀번호로 로그인합니다.")
    @PostMapping("/auth")
    public ResponseEntity<ApiResponseDto<SignupData>> auth(@Valid @RequestBody LoginRequest req) {
        UserResponse user = userService.login(req);
        String dummyAccessToken = "ACCESS-TOKEN-" + user.getId();
        return responseFactory.ok(new SignupData(user.getId(), dummyAccessToken));
    }

    /** 이메일 중복 확인 */
    @Operation(summary = "이메일 중복 확인 API", description = "이메일의 중복 여부를 확인합니다.")
    @GetMapping("/check-email")
    public ResponseEntity<ApiResponseDto<CheckEmailData>> checkEmail(@RequestParam @Email String email) {
        boolean available = userService.isEmailAvailable(email);
        if (!available) {
            throw new BusinessException(ErrorCode.EMAIL_DUPLICATE);
        }
        return responseFactory.ok(new CheckEmailData(true));
    }

    /** 회원 정보 조회 */
    @Operation(summary = "회원 정보 조회", description = "현재 로그인한 사용자의 정보를 조회합니다.")
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponseDto<UserResponse>> getUserInfo(@PathVariable Long userId) {
        UserResponse user = userService.getUserInfo(userId);
        return responseFactory.ok(user);
    }

    /** 회원 정보 수정 */
    @Operation(summary = "회원 정보 수정", description = "닉네임, 프로필 이미지를 수정합니다.")
    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponseDto<UserResponse>> updateUser(
            @PathVariable Long userId,
            @RequestBody UserUpdateRequest req
    ) {
        UserResponse updated = userService.updateUser(userId, req);
        return responseFactory.ok(updated);
    }

    /** 회원 탈퇴 */
    @Operation(summary = "회원 탈퇴", description = "회원 상태를 탈퇴로 변경합니다.")
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponseDto<Void>> withdraw(@PathVariable Long userId) {
        userService.withdraw(userId);
        return responseFactory.ok(null);
    }

    /** 비밀번호 변경 */
    @Operation(summary = "비밀번호 변경", description = "현재 비밀번호를 확인하고 새 비밀번호로 변경합니다.")
    @PutMapping("/{userId}/password")
    public ResponseEntity<ApiResponseDto<Void>> updatePassword(
            @PathVariable Long userId,
            @Valid @RequestBody UserPasswordUpdateRequest req
    ) {
        userService.updatePassword(userId, req);
        return responseFactory.ok(null);
    }

    /** 내부 응답용 record DTOs */
    record SignupData(Long user_id, String access_token) {}
    record CheckEmailData(boolean available) {}
}
