package com.example.WEEK04.model.dto.response;

import com.example.WEEK04.model.entity.Comment;

public class CommentResponse {

    private final Long comment_id;
    private final Long post_id;
    private final String content;
    private final String created_at;

    // 생성자 (Entity → DTO 변환)
    public CommentResponse(Comment comment) {
        this.comment_id = comment.getId();
        this.post_id = comment.getPostId();
        this.content = comment.getContent();
        this.created_at = comment.getCreatedAt();
    }

    // Getter
    public Long getComment_id() { return comment_id; }
    public Long getPost_id() { return post_id; }
    public String getContent() { return content; }
    public String getCreated_at() { return created_at; }
}
