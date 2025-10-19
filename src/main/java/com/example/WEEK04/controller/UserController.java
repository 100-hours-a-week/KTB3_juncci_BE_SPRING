package com.example.WEEK04.controller;

import com.example.WEEK04.common.ResponseFactory;
import com.example.WEEK04.exception.BusinessException;
import com.example.WEEK04.exception.ErrorCode;
import com.example.WEEK04.model.dto.request.LoginRequest;
import com.example.WEEK04.model.dto.request.SignupRequest;
import com.example.WEEK04.model.entity.User;
import com.example.WEEK04.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final ResponseFactory responseFactory; // ✅ 추가

    public UserController(UserService userService, ResponseFactory responseFactory) {
        this.userService = userService;
        this.responseFactory = responseFactory;
    }

    // ✅ 회원가입
    @PostMapping
    public ResponseEntity<?> register(@RequestBody SignupRequest req) {
        User user = userService.signup(req);
        return responseFactory.created(new SignupData(user.getId(), null));
    }

    // ✅ 로그인
    @PostMapping("/auth")
    public ResponseEntity<?> auth(@RequestBody LoginRequest req) {
        User user = userService.login(req);
        String dummyAccessToken = "ACCESS-TOKEN-" + user.getId();
        return responseFactory.ok(new SignupData(user.getId(), dummyAccessToken));
    }

    // ✅ 이메일 중복 확인
    @GetMapping("/check-email")
    public ResponseEntity<?> checkEmail(@RequestParam String email) {
        boolean available = userService.isEmailAvailable(email);
        if (!available) {
            throw new BusinessException(ErrorCode.EMAIL_DUPLICATE);
        }
        return responseFactory.ok(new CheckEmailData(true));
    }

    // ✅ 내부 DTO 구조 정리
    record SignupData(Long user_id, String access_token) {}
    record CheckEmailData(boolean available) {}
}
