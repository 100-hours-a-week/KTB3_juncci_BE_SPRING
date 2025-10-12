package com.example.WEEK04.service;

import com.example.WEEK04.exception.BusinessException;
import com.example.WEEK04.exception.ErrorCode;
import com.example.WEEK04.model.entity.User;
import com.example.WEEK04.model.dto.request.LoginRequest;
import com.example.WEEK04.model.dto.request.SignupRequest;
import com.example.WEEK04.repository.UserRepository;
import com.example.WEEK04.validation.UserValidator;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository repo;
    private final UserValidator validator;

    // DIP: 구체 클래스 직접 생성 X → 스프링이 주입
    public UserService(UserRepository repo, UserValidator validator) {
        this.repo = repo;
        this.validator = validator;
    }

    // 회원가입
    public User signup(SignupRequest req) {
        validator.validate(req);

        if (repo.findByEmail(req.getEmail()).isPresent()) {
            throw new BusinessException(ErrorCode.EMAIL_DUPLICATE);
        }

        return repo.save(req.getEmail(), req.getPassword(),
                req.getNickname(), req.getProfile_image());
    }

    // 로그인
    public User login(LoginRequest req) {
        if (req.getEmail() == null || req.getPassword() == null ||
                req.getEmail().isBlank() || req.getPassword().isBlank()) {
            throw new BusinessException(ErrorCode.FIELD_MISSING);
        }

        // 이메일 형식 검증
        if (!req.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new BusinessException(ErrorCode.EMAIL_INVALID);
        }

        User user = repo.findByEmail(req.getEmail())
                .orElseThrow(() -> new BusinessException(ErrorCode.LOGIN_FAIL));

        if (!user.getPassword().equals(req.getPassword())) {
            throw new BusinessException(ErrorCode.LOGIN_FAIL);
        }

        return user;
    }

    // 이메일 중복 확인
    public boolean isEmailAvailable(String email) {
        // 형식 검증
        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new BusinessException(ErrorCode.EMAIL_INVALID);
        }

        // true → 사용 가능, false → 이미 사용 중
        return repo.findByEmail(email).isEmpty();
    }
}
