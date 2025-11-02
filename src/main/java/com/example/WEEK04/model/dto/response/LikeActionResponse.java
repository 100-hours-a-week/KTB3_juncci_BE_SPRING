package com.example.WEEK04.model.dto.response;

public class LikeActionResponse {
    private final Long post_id;
    private final String message;

    public LikeActionResponse(Long postId, String message) {
        this.post_id = postId;
        this.message = message;
    }

    public Long getPost_id() { return post_id; }
    public String getMessage() { return message; }
}
