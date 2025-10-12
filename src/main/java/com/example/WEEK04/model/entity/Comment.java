package com.example.WEEK04.model.entity;

import java.time.LocalDateTime;

public class Comment {
    private Long id;
    private Long postId;
    private Long authorId;
    private String content;
    private String createdAt;

    public Comment(Long id, Long postId, Long authorId, String content) {
        this.id = id;
        this.postId = postId;
        this.authorId = authorId;
        this.content = content;
        this.createdAt = LocalDateTime.now().toString();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getPostId() { return postId; }
    public Long getAuthorId() { return authorId; }
    public String getContent() { return content; }
    public String getCreatedAt() { return createdAt; }
}
