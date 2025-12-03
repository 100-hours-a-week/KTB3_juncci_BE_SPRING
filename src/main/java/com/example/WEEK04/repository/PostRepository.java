package com.example.WEEK04.repository;

import com.example.WEEK04.model.dto.PostSummaryDto;
import com.example.WEEK04.model.entity.Post;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    /** 🔥 조회수 증가 (동시성 안전) */
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("UPDATE Post p SET p.viewCount = p.viewCount + 1 WHERE p.id = :postId")
    void incrementViewCount(@Param("postId") Long postId);


    /** 🔥 게시글 목록 조회 (페이징 + 유저 join + DTO Projection)
     *  N+1 없이 단 1쿼리로 리스트 반환
     */
    @Query("""
        SELECT new com.example.WEEK04.model.dto.PostSummaryDto(
            p.id,
            p.title,
            p.createdAt,
            p.commentCount,
            p.likeCount,
            p.viewCount,
            u.id,
            u.nickname
        )
        FROM Post p
        JOIN p.user u
    """)
    Page<PostSummaryDto> findPostSummaries(Pageable pageable);


    /** 🔥 게시글 상세 조회 (fetch join, 페이징 불가능 → OK) */
    @Query("""
        SELECT DISTINCT p FROM Post p
        LEFT JOIN FETCH p.user
        LEFT JOIN FETCH p.comments
        WHERE p.id = :id
    """)
    Optional<Post> findPostWithDetails(@Param("id") Long id);


    /** 내가 쓴 글 목록 (N+1 거의 없음 — 같은 user라서 user 로딩 1회뿐)
     *  필요하면 이것도 Projection 방식으로 바꿔줄 수 있음
     */
    Page<Post> findAllByUserId(Long userId, Pageable pageable);

    @Query("""
SELECT new com.example.WEEK04.model.dto.PostSummaryDto(
    p.id,
    p.title,
    p.createdAt,
    p.commentCount,
    p.likeCount,
    p.viewCount,
    u.id,
    u.nickname
)
FROM Post p
JOIN p.user u
WHERE u.id = :userId
""")
    Page<PostSummaryDto> findPostSummariesByUserId(
            @Param("userId") Long userId,
            Pageable pageable
    );


}
