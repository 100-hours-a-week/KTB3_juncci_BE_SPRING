package com.example.WEEK04.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "posts")
@BatchSize(size = 50)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    private String images;
    private int viewCount;
    private int likeCount;
    private int commentCount;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    /** ===== 연관관계 ===== */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false) // 작성자 정보도 필수
    private User user;

    @JsonIgnore
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Like> likes = new ArrayList<>();

    /** ===== 생성자 ===== */
    public Post(User user, String title, String content, String images) {
        this.user = user;
        this.title = title;
        this.content = content;
        this.images = images;
    }

    /** ===== 편의 메서드 ===== */
    public void setUser(User user) { this.user = user; }

    public void addComment(Comment comment) {
        comments.add(comment);
        comment.setPost(this);
        incrementCommentCount();
    }

    public void addLike(Like like) {
        likes.add(like);
        like.setPost(this);
        incrementLikeCount();
    }

    public void removeComment(Comment comment) {
        comments.remove(comment);
        comment.setPost(null);
        decrementCommentCount();
    }

    public void removeLike(Like like) {
        likes.remove(like);
        like.setPost(null);
        decrementLikeCount();
    }

    /** ===== Count 관리 ===== */
    public void incrementLikeCount() { this.likeCount++; }
    public void decrementLikeCount() { if (likeCount > 0) likeCount--; }
    public void incrementCommentCount() { this.commentCount++; }
    public void decrementCommentCount() { if (commentCount > 0) commentCount--; }
    public void incrementViewCount() { this.viewCount++; }

    /** ===== 업데이트 ===== */
    public void updateTitle(String title) { this.title = title; }
    public void updateContent(String content) { this.content = content; }
    public void updateImages(String images) { this.images = images; }
}
