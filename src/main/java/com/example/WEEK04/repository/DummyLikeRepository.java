package com.example.WEEK04.repository;

import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public class DummyLikeRepository {

    // postId → set of userIds
    private final Map<Long, Set<Long>> likes = new HashMap<>();

    public boolean hasLiked(Long postId, Long userId) {
        return likes.getOrDefault(postId, Collections.emptySet()).contains(userId);
    }

    public void addLike(Long postId, Long userId) {
        likes.computeIfAbsent(postId, k -> new HashSet<>()).add(userId);
    }

    public void removeLike(Long postId, Long userId) {
        Set<Long> userLikes = likes.get(postId);
        if (userLikes != null) {
            userLikes.remove(userId);
            if (userLikes.isEmpty()) {
                likes.remove(postId);
            }
        }
    }

    public int getLikeCount(Long postId) {
        return likes.getOrDefault(postId, Collections.emptySet()).size();
    }
}
