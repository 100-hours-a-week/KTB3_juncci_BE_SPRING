package com.example.WEEK04.model.entity;

import com.example.WEEK04.model.enums.UserStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "users")
@BatchSize(size = 50)
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String password;
    private String nickname;
    private String profileImage;

    /** 회원 상태 추가 */
    @Enumerated(EnumType.STRING)
    private UserStatus status = UserStatus.ACTIVE;

    /** ===== 연관관계 ===== */
    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Like> likes = new ArrayList<>();

    /** ===== 생성자 ===== */
    public User(String email, String password, String nickname, String profileImage) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.status = UserStatus.ACTIVE;
    }

    /** ===== 연관관계 편의 메서드 ===== */
    public void addPost(Post post) {
        posts.add(post);
        post.setUser(this);
    }

    public void addComment(Comment comment) {
        comments.add(comment);
        comment.setAuthor(this);
    }

    public void addLike(Like like) {
        likes.add(like);
        like.setUser(this);
    }

    public void removeComment(Comment comment) {
        comments.remove(comment);
        comment.setAuthor(null);
    }

    public void removeLike(Like like) {
        likes.remove(like);
        like.setUser(null);
    }

    /** ===== 비즈니스 로직 ===== */

    // 닉네임 수정
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    // 프로필 이미지 수정
    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    // 비밀번호 변경
    public void setPassword(String password) {
        this.password = password;
    }

    // 상태 변경
    public void setStatus(UserStatus status) {
        this.status = status;
    }

    // 탈퇴 처리
    public void withdraw() {
        this.status = UserStatus.WITHDRAWN;
    }
}
