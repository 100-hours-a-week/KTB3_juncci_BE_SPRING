package com.example.WEEK04.repository;

import com.example.WEEK04.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /** 이메일 중복 확인 또는 로그인 시 사용 */
    Optional<User> findByEmail(String email);
}
