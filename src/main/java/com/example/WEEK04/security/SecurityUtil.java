package com.example.WEEK04.security;

import com.example.WEEK04.exception.BusinessException;
import com.example.WEEK04.exception.ErrorCode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {

    /** 현재 로그인한 사용자 ID (JWT subject)를 Long으로 반환 */
    public static Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || auth.getName() == null) {
            throw new BusinessException(ErrorCode.AUTH_TOKEN_INVALID);
        }

        try {
            // TokenProvider에서 subject를 userId(String) 로 넣어놨음
            return Long.parseLong(auth.getName());
        } catch (NumberFormatException e) {
            throw new BusinessException(ErrorCode.AUTH_TOKEN_INVALID);
        }
    }
}
