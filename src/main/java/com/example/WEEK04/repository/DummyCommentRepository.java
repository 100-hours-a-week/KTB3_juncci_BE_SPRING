package com.example.WEEK04.repository;

import com.example.WEEK04.model.entity.Comment;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class DummyCommentRepository {

    private final Map<Long, Comment> comments = new ConcurrentHashMap<>();
    private long sequence = 0L;

    public Comment save(Comment comment) {
        if (comment.getId() == null) {
            comment.setId(++sequence);
        }
        comments.put(comment.getId(), comment);
        return comment;
    }

    public Optional<Comment> findById(Long id) {
        return Optional.ofNullable(comments.get(id));
    }

    public List<Comment> findByPostId(Long postId) {
        return comments.values().stream()
                .filter(c -> c.getPostId().equals(postId))
                .collect(Collectors.toList());
    }

    public boolean deleteById(Long id) {
        return comments.remove(id) != null;
    }
}
