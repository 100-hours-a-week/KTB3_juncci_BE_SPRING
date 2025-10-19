package com.example.WEEK04.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "댓글 수정 요청 DTO")
public class CommentUpdateRequest {

    @Schema(description = "댓글 내용", example = "수정된 댓글 내용입니다.")
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
