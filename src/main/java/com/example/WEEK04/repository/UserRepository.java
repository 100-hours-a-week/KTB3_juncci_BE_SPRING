package com.example.WEEK04.repository;

import com.example.WEEK04.model.entity.User;
import java.util.Optional;

public interface UserRepository {
    Optional<User> findByEmail(String email);
    User save(String email, String password, String nickname, String profileImage);
}
