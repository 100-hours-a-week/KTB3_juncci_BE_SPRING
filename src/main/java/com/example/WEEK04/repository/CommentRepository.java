package com.example.WEEK04.repository;

import com.example.WEEK04.model.entity.Comment;
import com.example.WEEK04.model.entity.Post;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByPost(Post post);

    @EntityGraph(attributePaths = {"author"})
    @Query("SELECT c FROM Comment c WHERE c.post = :post")
    List<Comment> findByPostWithAuthor(Post post);

    int countByPost(Post post);
}
