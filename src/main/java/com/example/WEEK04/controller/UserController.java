package com.example.WEEK04.controller;

import com.example.WEEK04.common.ApiResponseDto;
import com.example.WEEK04.common.ResponseFactory;
import com.example.WEEK04.exception.BusinessException;
import com.example.WEEK04.exception.ErrorCode;
import com.example.WEEK04.model.dto.request.*;
import com.example.WEEK04.model.dto.response.UserResponse;
import com.example.WEEK04.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final ResponseFactory responseFactory;
    private final AuthenticationManager authenticationManager;

    /** 회원가입 */
    @PostMapping
    public ResponseEntity<ApiResponseDto<SignupData>> register(
            @Valid @RequestBody SignupRequest req)
    {
        UserResponse user = userService.signup(req);
        return responseFactory.created(new SignupData(user.getId()));
    }

    /** 로그인 (세션 기반 인증) */
    @PostMapping("/auth")
    public ResponseEntity<ApiResponseDto<Void>> login(
            @Valid @RequestBody LoginRequest req,
            HttpServletRequest request
    ) {

        // Spring Security 인증 토큰 생성
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword());

        // 인증 수행 → UserDetailsService 실행됨
        Authentication authentication = authenticationManager.authenticate(authToken);

        // SecurityContext 생성 & 저장
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        // 세션에 SecurityContext 저장 → JSESSIONID 발급됨
        HttpSession session = request.getSession(true);
        session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, context);

        return responseFactory.ok(null);
    }

    /** 이메일 중복 확인 */
    @GetMapping("/check-email")
    public ResponseEntity<ApiResponseDto<CheckEmailData>> checkEmail(
            @RequestParam @Email String email)
    {
        boolean available = userService.isEmailAvailable(email);
        if (!available) {
            throw new BusinessException(ErrorCode.EMAIL_DUPLICATE);
        }
        return responseFactory.ok(new CheckEmailData(true));
    }

    /** 회원 정보 조회 */
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponseDto<UserResponse>> getUserInfo(
            @PathVariable Long userId)
    {
        UserResponse user = userService.getUserInfo(userId);
        return responseFactory.ok(user);
    }

    /** 회원 정보 수정 */
    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponseDto<UserResponse>> updateUser(
            @PathVariable Long userId,
            @RequestBody UserUpdateRequest req)
    {
        UserResponse updated = userService.updateUser(userId, req);
        return responseFactory.ok(updated);
    }

    /** 회원 탈퇴 */
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponseDto<Void>> withdraw(
            @PathVariable Long userId)
    {
        userService.withdraw(userId);
        return responseFactory.ok(null);
    }

    /** 비밀번호 변경 */
    @PutMapping("/{userId}/password")
    public ResponseEntity<ApiResponseDto<Void>> updatePassword(
            @PathVariable Long userId,
            @Valid @RequestBody UserPasswordUpdateRequest req)
    {
        userService.updatePassword(userId, req);
        return responseFactory.ok(null);
    }

    /** 내부 DTOs */
    record SignupData(Long user_id) {}
    record CheckEmailData(boolean available) {}
}
