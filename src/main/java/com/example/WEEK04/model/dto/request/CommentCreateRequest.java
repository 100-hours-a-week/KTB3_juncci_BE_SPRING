package com.example.WEEK04.model.dto.request;

import jakarta.validation.constraints.NotBlank;

public class CommentCreateRequest {

    @NotBlank(message = "댓글 내용을 입력해주세요.")
    private String content;

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}
