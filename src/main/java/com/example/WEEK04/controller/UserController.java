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
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final ResponseFactory responseFactory;

    /** 회원가입 */
    @Operation(summary = "회원가입 API", description = "새로운 사용자를 등록합니다.")
    @PostMapping
    public ResponseEntity<ApiResponseDto<SignupData>> register(
            @Valid @RequestBody SignupRequest req
    ) {
        UserResponse user = userService.signup(req);
        return responseFactory.created(new SignupData(user.getId(), null));
    }

    /** 이메일 중복 확인 */
    @Operation(summary = "이메일 중복 확인 API", description = "이메일의 중복 여부를 확인합니다.")
    @GetMapping("/check-email")
    public ResponseEntity<ApiResponseDto<CheckEmailData>> checkEmail(
            @RequestParam @Email String email
    ) {
        boolean available = userService.isEmailAvailable(email);
        if (!available) {
            throw new BusinessException(ErrorCode.EMAIL_DUPLICATE);
        }
        return responseFactory.ok(new CheckEmailData(true));
    }

    /** 내 정보 조회 */
    @Operation(summary = "내 정보 조회", description = "JWT 기반으로 로그인한 본인의 정보를 조회합니다.")
    @GetMapping("/me")
    public ResponseEntity<ApiResponseDto<UserResponse>> getMyInfo() {
        UserResponse user = userService.getMyInfo();
        return responseFactory.ok(user);
    }

    /** 내 정보 수정 */
    @Operation(summary = "내 정보 수정", description = "JWT 로그인된 본인의 닉네임/프로필 이미지를 수정합니다.")
    @PutMapping("/me")
    public ResponseEntity<ApiResponseDto<UserResponse>> updateMyInfo(
            @RequestBody UserUpdateRequest req
    ) {
        UserResponse updated = userService.updateMyInfo(req);
        return responseFactory.ok(updated);
    }

    /** 내 비밀번호 변경 */
    @Operation(summary = "내 비밀번호 변경", description = "로그인한 본인의 비밀번호를 변경합니다.")
    @PutMapping("/me/password")
    public ResponseEntity<ApiResponseDto<Void>> updatePassword(
            @Valid @RequestBody UserPasswordUpdateRequest req
    ) {
        userService.updatePassword(req);
        return responseFactory.ok(null);
    }

    /** 회원 탈퇴 */
    @Operation(summary = "회원 탈퇴", description = "본인 계정을 탈퇴 처리합니다.")
    @DeleteMapping("/me")
    public ResponseEntity<ApiResponseDto<Void>> withdraw() {
        userService.withdrawMe();
        return responseFactory.ok(null);
    }

    /** 내부 DTO */
    record SignupData(Long user_id, String access_token) {}
    record CheckEmailData(boolean available) {}
}
