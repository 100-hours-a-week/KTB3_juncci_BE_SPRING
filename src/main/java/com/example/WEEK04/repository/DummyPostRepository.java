package com.example.WEEK04.repository;

import com.example.WEEK04.model.entity.Post;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class DummyPostRepository {

    private final Map<Long, Post> posts = new ConcurrentHashMap<>();
    private long sequence = 0L;

    public Post save(Post post) {
        Post saved = post;
        if (post.getId() == null) {
            saved = new Post(++sequence, post.getAuthorId(), post.getTitle(), post.getContent(), post.getImages());
        }
        posts.put(saved.getId(), saved);
        return saved;
    }

    public Optional<Post> findById(Long id) {
        return Optional.ofNullable(posts.get(id));
    }

    public List<Post> findAll(int page, int size, String sort) {
        if (page < 1 || size <= 0) {
            throw new IllegalArgumentException("page와 size는 1 이상이어야 합니다.");
        }

        Comparator<Post> comparator = Comparator.comparing(Post::getId);
        if ("desc".equalsIgnoreCase(sort)) {
            comparator = comparator.reversed();
        }

        return posts.values().stream()
                .sorted(comparator)
                .skip((long) (page - 1) * size)
                .limit(size)
                .collect(Collectors.toList());
    }

    public int count() {
        return posts.size();
    }

    public boolean deleteById(Long id) {
        return posts.remove(id) != null;
    }
}
