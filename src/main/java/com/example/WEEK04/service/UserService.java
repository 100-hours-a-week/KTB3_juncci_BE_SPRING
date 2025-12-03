package com.example.WEEK04.service;

import com.example.WEEK04.exception.BusinessException;
import com.example.WEEK04.exception.ErrorCode;
import com.example.WEEK04.model.dto.request.*;
import com.example.WEEK04.model.dto.response.UserResponse;
import com.example.WEEK04.model.entity.User;
import com.example.WEEK04.model.enums.UserStatus;
import com.example.WEEK04.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repo;
    private final PasswordEncoder passwordEncoder;

    /** 회원가입 */
    public UserResponse signup(SignupRequest req) {
        if (repo.findByEmail(req.getEmail()).isPresent()) {
            throw new BusinessException(ErrorCode.EMAIL_DUPLICATE);
        }

        String encodedPassword = passwordEncoder.encode(req.getPassword());

        User user = new User(
                req.getEmail(),
                encodedPassword,
                req.getNickname(),
                req.getProfile_image()
        );

        User savedUser = repo.save(user);
        return new UserResponse(savedUser);
    }

    /** 이메일 중복 확인 */
    @Transactional(readOnly = true)
    public boolean isEmailAvailable(String email) {
        return repo.findByEmail(email).isEmpty();
    }

    /** 현재 로그인한 사용자 ID 가져오기 */
    private Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || auth.getName() == null) {
            throw new BusinessException(ErrorCode.AUTH_TOKEN_INVALID);
        }

        try {
            return Long.parseLong(auth.getName()); // TokenProvider에서 subject를 userId로 넣어둠
        } catch (NumberFormatException e) {
            throw new BusinessException(ErrorCode.AUTH_TOKEN_INVALID);
        }
    }

    /** 내 정보 조회 */
    @Transactional(readOnly = true)
    public UserResponse getMyInfo() {
        Long userId = getCurrentUserId();

        User user = repo.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        return new UserResponse(user);
    }

    /** 내 정보 수정 */
    public UserResponse updateMyInfo(UserUpdateRequest req) {
        Long userId = getCurrentUserId();

        User user = repo.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        boolean changed = false;

        if (req.getNickname() != null && !req.getNickname().isBlank()) {
            user.setNickname(req.getNickname());
            changed = true;
        }

        if (req.getProfileImage() != null && !req.getProfileImage().isBlank()) {
            user.setProfileImage(req.getProfileImage());
            changed = true;
        }

        if (changed) repo.save(user);

        return new UserResponse(user);
    }

    /** 내 비밀번호 변경 */
    public void updatePassword(UserPasswordUpdateRequest req) {
        Long userId = getCurrentUserId();

        User user = repo.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(req.getCurrentPassword(), user.getPassword())) {
            throw new BusinessException(ErrorCode.PASSWORD_INVALID);
        }

        user.setPassword(passwordEncoder.encode(req.getNewPassword()));
        repo.save(user);
    }

    /** 회원 탈퇴 */
    public void withdrawMe() {
        Long userId = getCurrentUserId();

        User user = repo.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        user.withdraw(); // 상태 변경
        repo.save(user);
    }
}
