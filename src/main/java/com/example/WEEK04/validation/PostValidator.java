package com.example.WEEK04.validation;

import com.example.WEEK04.exception.BusinessException;
import com.example.WEEK04.exception.ErrorCode;
import com.example.WEEK04.model.dto.request.PostUpdateRequest;
import com.example.WEEK04.model.dto.request.PostCreateRequest;
import org.springframework.stereotype.Component;

@Component
public class PostValidator {

    public void validateCreate(PostCreateRequest req) {
        if (req.getTitle() == null || req.getTitle().isBlank() ||
                req.getContent() == null || req.getContent().isBlank()) {
            throw new BusinessException(ErrorCode.POST_FIELD_MISSING);
        }
        if (req.getTitle().length() > 26) {
            throw new BusinessException(ErrorCode.POST_TITLE_LEN);
        }
    }

    public void validateUpdate(PostUpdateRequest req) {
        if (req.getTitle() == null || req.getTitle().isBlank() ||
                req.getContent() == null || req.getContent().isBlank()) {
            throw new BusinessException(ErrorCode.POST_FIELD_MISSING);
        }
        if (req.getTitle().length() > 26) {
            throw new BusinessException(ErrorCode.POST_TITLE_LEN);
        }
    }
}
