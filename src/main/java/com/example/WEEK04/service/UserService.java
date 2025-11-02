package com.example.WEEK04.service;

import com.example.WEEK04.exception.BusinessException;
import com.example.WEEK04.exception.ErrorCode;
import com.example.WEEK04.model.dto.request.*;
import com.example.WEEK04.model.dto.response.UserResponse;
import com.example.WEEK04.model.entity.User;
import com.example.WEEK04.model.enums.UserStatus;
import com.example.WEEK04.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {

    private final UserRepository repo;

    public UserService(UserRepository repo) {
        this.repo = repo;
    }

    /** 회원가입 */
    public UserResponse signup(SignupRequest req) {
        if (repo.findByEmail(req.getEmail()).isPresent()) {
            throw new BusinessException(ErrorCode.EMAIL_DUPLICATE);
        }

        User user = new User(
                req.getEmail(),
                req.getPassword(),
                req.getNickname(),
                req.getProfile_image()
        );

        User savedUser = repo.save(user);
        return new UserResponse(savedUser);
    }

    /** 로그인 */
    public UserResponse login(LoginRequest req) {
        User user = repo.findByEmail(req.getEmail())
                .orElseThrow(() -> new BusinessException(ErrorCode.LOGIN_FAIL));

        if (!user.getPassword().equals(req.getPassword())) {
            throw new BusinessException(ErrorCode.LOGIN_FAIL);
        }

        if (user.getStatus() == UserStatus.WITHDRAWN) {
            throw new BusinessException(ErrorCode.USER_WITHDRAWN);
        }

        return new UserResponse(user);
    }

    /** 이메일 중복 확인 */
    @Transactional(readOnly = true)
    public boolean isEmailAvailable(String email) {
        return repo.findByEmail(email).isEmpty();
    }

    /** 회원 정보 조회 */
    @Transactional(readOnly = true)
    public UserResponse getUserInfo(Long id) {
        User user = repo.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        return new UserResponse(user);
    }

    /** 회원 정보 수정 (닉네임, 프로필 이미지) */
    public UserResponse updateUser(Long id, UserUpdateRequest req) {
        User user = repo.findById(id)
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

        if (changed) {
            user.setStatus(UserStatus.ACTIVE);
            repo.save(user);
        }

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

        if (!user.getPassword().equals(req.getCurrentPassword())) {
            throw new BusinessException(ErrorCode.PASSWORD_INVALID);
        }

        user.setPassword(req.getNewPassword());
        repo.save(user);
    }
}
