package com.example.WEEK04.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "post_likes")
@BatchSize(size = 50)
public class Like {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Like(Post post, User user) {
        this.post = post;
        this.user = user;
    }

    public void setPost(Post post) { this.post = post; }
    public void setUser(User user) { this.user = user; }
}
