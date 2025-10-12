package com.example.WEEK04.controller;

import com.example.WEEK04.exception.BusinessException;
import com.example.WEEK04.exception.ErrorCode;
import com.example.WEEK04.model.entity.User;
import com.example.WEEK04.model.dto.request.LoginRequest;
import com.example.WEEK04.model.dto.request.SignupRequest;
import com.example.WEEK04.model.dto.response.CheckEmailResponse;
import com.example.WEEK04.model.dto.response.SignupResponse;
import com.example.WEEK04.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    public UserController(UserService userService) { this.userService = userService; }

    // 회원가입
    @PostMapping
    public ResponseEntity<SignupResponse> register(@RequestBody SignupRequest req) {
        User user = userService.signup(req);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new SignupResponse("register_success", new Data(user.getId()), null));
    }

    // 로그인
    @PostMapping("/auth")
    public ResponseEntity<SignupResponse> auth(@RequestBody LoginRequest req) {
        User user = userService.login(req);
        String dummyAccessToken = "ACCESS-TOKEN-" + user.getId();

        return ResponseEntity.ok(
                new SignupResponse(
                        "login_success",
                        new Data(user.getId(), dummyAccessToken),
                        null
                )
        );
    }

    // 이메일 중복 확인
    @GetMapping("/check-email")
    public ResponseEntity<CheckEmailResponse> checkEmail(@RequestParam String email) {
        boolean available = userService.isEmailAvailable(email);
        if (!available) {
            throw new BusinessException(ErrorCode.EMAIL_DUPLICATE);
        }

        return ResponseEntity.ok(
                new CheckEmailResponse("ok", new CheckEmailResponse.Data(true), null)
        );
    }

    private static class Data {
        private final Long user_id;
        private final String access_token;

        public Data(Long id) { this.user_id = id; this.access_token = null; }
        public Data(Long id, String token) { this.user_id = id; this.access_token = token; }

        public Long getUser_id() { return user_id; }
        public String getAccess_token() { return access_token; }
    }
}
