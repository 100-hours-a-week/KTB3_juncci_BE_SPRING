package com.example.WEEK04.service;

import com.example.WEEK04.exception.BusinessException;
import com.example.WEEK04.exception.ErrorCode;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    public Long extractUserId(String authorization) {
        if (authorization == null || !authorization.startsWith("Bearer ACCESS-TOKEN-")) {
            throw new BusinessException(ErrorCode.AUTH_TOKEN_INVALID);
        }

        try {
            return Long.parseLong(authorization.replace("Bearer ACCESS-TOKEN-", ""));
        } catch (NumberFormatException e) {
            throw new BusinessException(ErrorCode.AUTH_TOKEN_INVALID);
        }
    }
}
