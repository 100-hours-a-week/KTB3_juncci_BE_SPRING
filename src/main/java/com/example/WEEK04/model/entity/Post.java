package com.example.WEEK04.model.entity;

import java.util.List;

public class Post {
    private Long id;
    private Long authorId;
    private String title;
    private String content;
    private List<String> images;
    private String createdAt;
    private int commentCount;
    private int likeCount;
    private int viewCount;

    // 기존 생성자
    public Post(Long id, Long authorId, String title, String content, List<String> images) {
        this.id = id;
        this.authorId = authorId;
        this.title = title;
        this.content = content;
        this.images = images;
        this.createdAt = java.time.LocalDateTime.now().toString();
        this.commentCount = 0;
        this.likeCount = 0;
        this.viewCount = 0;
    }

    // getter/setter
    public Long getId() { return id; }
    public Long getAuthorId() { return authorId; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public List<String> getImages() { return images; }
    public String getCreatedAt() { return createdAt; }
    public int getCommentCount() { return commentCount; }
    public int getLikeCount() { return likeCount; }
    public int getViewCount() { return viewCount; }

    public void setId(Long id) { this.id = id; }

    // 좋아요 개수 setter 추가
    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    //
    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }
}
