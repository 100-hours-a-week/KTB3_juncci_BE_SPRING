package com.example.WEEK04.service;

import com.example.WEEK04.exception.BusinessException;
import com.example.WEEK04.exception.ErrorCode;
import com.example.WEEK04.model.dto.request.*;
import com.example.WEEK04.model.dto.response.UserResponse;
import com.example.WEEK04.model.entity.User;
import com.example.WEEK04.model.enums.UserStatus;
import com.example.WEEK04.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.security.core.Authentication;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repo;
    private final AuthenticationManager authManager;
    private final PasswordEncoder passwordEncoder;

    /** 회원가입 */
    public UserResponse signup(SignupRequest req) {
        if (repo.findByEmail(req.getEmail()).isPresent()) {
            throw new BusinessException(ErrorCode.EMAIL_DUPLICATE);
        }

        User user = new User(
                req.getEmail(),
                passwordEncoder.encode(req.getPassword()),  // 비밀번호 암호화
                req.getNickname(),
                req.getProfile_image()
        );

        User savedUser = repo.save(user);
        return new UserResponse(savedUser);
    }

    /** 로그인 → 세션 생성됨 (토큰 아님) */
    public void login(LoginRequest req) {

        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword());

        Authentication auth;

        try {
            auth = authManager.authenticate(token);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.LOGIN_FAIL);
        }

        // 인증 정보 저장
        SecurityContextHolder.getContext().setAuthentication(auth);

        // ★★ 세션 생성 (절대 빠지면 안 됨) ★★
        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attributes == null) {
            throw new IllegalStateException("RequestContextHolder is null. " +
                    "login()은 반드시 HTTP 요청 안에서 호출되어야 합니다.");
        }

        HttpServletRequest request = attributes.getRequest();
        request.getSession(true); // 세션 생성 (JSESSIONID 발급)
    }



    /** 이메일 중복 확인 */
    @Transactional
    public boolean isEmailAvailable(String email) {
        return repo.findByEmail(email).isEmpty();
    }

    /** 회원 정보 조회 */
    @Transactional
    public UserResponse getUserInfo(Long id) {
        User user = repo.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        return new UserResponse(user);
    }

    /** 회원 정보 수정 */
    public UserResponse updateUser(Long id, UserUpdateRequest req) {
        User user = repo.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        if (req.getNickname() != null && !req.getNickname().isBlank()) {
            user.setNickname(req.getNickname());
        }

        if (req.getProfileImage() != null && !req.getProfileImage().isBlank()) {
            user.setProfileImage(req.getProfileImage());
        }

        repo.save(user);
        return new UserResponse(user);
    }

    /** 회원 탈퇴 */
    public void withdraw(Long id) {
        User user = repo.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        user.withdraw();
        repo.save(user);
    }

    /** 비밀번호 변경 */
    public void updatePassword(Long id, UserPasswordUpdateRequest req) {
        User user = repo.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(req.getCurrentPassword(), user.getPassword())) {
            throw new BusinessException(ErrorCode.PASSWORD_INVALID);
        }

        user.setPassword(passwordEncoder.encode(req.getNewPassword()));
        repo.save(user);
    }
}
