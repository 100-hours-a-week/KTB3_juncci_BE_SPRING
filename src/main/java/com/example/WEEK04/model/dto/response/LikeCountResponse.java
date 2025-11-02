package com.example.WEEK04.model.dto.response;

public class LikeCountResponse {
    private final Long post_id;
    private final int like_count;

    public LikeCountResponse(Long postId, int likeCount) {
        this.post_id = postId;
        this.like_count = likeCount;
    }

    public Long getPost_id() { return post_id; }
    public int getLike_count() { return like_count; }
}
