package com.example.WEEK04.repository;

import com.example.WEEK04.model.entity.User;
import org.springframework.stereotype.Repository;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class DummyUserRepository implements UserRepository {
    private final Map<String, User> usersByEmail = new ConcurrentHashMap<>();
    private long idSeq = 1L;

    @Override
    public Optional<User> findByEmail(String email) {
        return Optional.ofNullable(usersByEmail.get(email));
    }

    @Override
    public User save(String email, String password, String nickname, String profileImage) {
        User u = new User(idSeq++, email, password, nickname, profileImage);
        usersByEmail.put(email, u);
        return u;
    }
}
