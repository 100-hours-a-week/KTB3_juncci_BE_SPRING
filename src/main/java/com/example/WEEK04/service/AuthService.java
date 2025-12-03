package com.example.WEEK04.service;

import com.example.WEEK04.exception.BusinessException;
import com.example.WEEK04.exception.ErrorCode;
import com.example.WEEK04.model.dto.request.LoginRequest;
import com.example.WEEK04.model.entity.User;
import com.example.WEEK04.repository.UserRepository;
import com.example.WEEK04.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;

    /** 로그인 → JWT 발급 */
    public String login(LoginRequest req) {

        // 이메일 확인
        User user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new BusinessException(ErrorCode.LOGIN_FAIL));

        // 비밀번호 검증 (BCrypt 매칭)
        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            throw new BusinessException(ErrorCode.LOGIN_FAIL);
        }

        // JWT 생성
        return tokenProvider.generateToken(user.getId());
    }
}
