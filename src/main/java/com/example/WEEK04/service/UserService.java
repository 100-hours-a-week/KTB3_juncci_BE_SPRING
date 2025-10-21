package com.example.WEEK04.service;

import com.example.WEEK04.exception.BusinessException;
import com.example.WEEK04.exception.ErrorCode;
import com.example.WEEK04.model.entity.User;
import com.example.WEEK04.model.dto.request.LoginRequest;
import com.example.WEEK04.model.dto.request.SignupRequest;
import com.example.WEEK04.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository repo;

    public UserService(UserRepository repo) {
        this.repo = repo;
    }

    // 회원가입
    public User signup(SignupRequest req) {
        if (repo.findByEmail(req.getEmail()).isPresent()) {
            throw new BusinessException(ErrorCode.EMAIL_DUPLICATE);
        }

        return repo.save(req.getEmail(), req.getPassword(),
                req.getNickname(), req.getProfile_image());
    }

    // 로그인
    public User login(LoginRequest req) {
        User user = repo.findByEmail(req.getEmail())
                .orElseThrow(() -> new BusinessException(ErrorCode.LOGIN_FAIL));

        if (!user.getPassword().equals(req.getPassword())) {
            throw new BusinessException(ErrorCode.LOGIN_FAIL);
        }

        return user;
    }

    // 이메일 중복 확인
    public boolean isEmailAvailable(String email) {
        return repo.findByEmail(email).isEmpty();
    }
}
