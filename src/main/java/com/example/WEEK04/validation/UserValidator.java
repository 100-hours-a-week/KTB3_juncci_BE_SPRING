package com.example.WEEK04.validation;

import com.example.WEEK04.exception.BusinessException;
import com.example.WEEK04.exception.ErrorCode;
import com.example.WEEK04.model.dto.request.SignupRequest;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class UserValidator {
    private static final Pattern EMAIL_REGEX =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    private static final Pattern PASSWORD_REGEX =
            Pattern.compile("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*]).{8,20}$");

    public void validate(SignupRequest req) {
        if (req.getEmail() == null || !EMAIL_REGEX.matcher(req.getEmail()).matches()) {
            throw new BusinessException(ErrorCode.EMAIL_INVALID);
        }
        if (req.getPassword() == null || !PASSWORD_REGEX.matcher(req.getPassword()).matches()) {
            throw new BusinessException(ErrorCode.PASSWORD_INVALID);
        }
        if (req.getNickname() == null || req.getNickname().isBlank()) {
            throw new BusinessException(ErrorCode.NICKNAME_INVALID);
        }
        if (req.getProfile_image() != null && !req.getProfile_image().startsWith("https://")) {
            throw new BusinessException(ErrorCode.PROFILE_IMAGE_INVALID);
        }
    }
}
