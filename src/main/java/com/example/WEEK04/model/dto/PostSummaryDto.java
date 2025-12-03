package com.example.WEEK04.model.dto;

import java.time.LocalDateTime;

public record PostSummaryDto(
        Long postId,
        String title,
        LocalDateTime createdAt,
        int commentCount,
        int likeCount,
        int viewCount,
        Long authorId,
        String authorNickname
) {}
