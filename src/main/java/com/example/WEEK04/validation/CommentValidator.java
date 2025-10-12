package com.example.WEEK04.validation;

import com.example.WEEK04.exception.BusinessException;
import com.example.WEEK04.exception.ErrorCode;
import com.example.WEEK04.model.dto.request.CommentCreateRequest;
import org.springframework.stereotype.Component;

@Component
public class CommentValidator {

    public void validateCreate(CommentCreateRequest req) {
        if (req.getContent() == null || req.getContent().isBlank()) {
            throw new BusinessException(ErrorCode.COMMENT_FIELD_MISSING);
        }
    }
}
