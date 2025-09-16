package com.workintech.s19c.repository;


import com.workintech.s19c.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    // Özel sorgular buraya eklenebilir, örneğin bir tweete ait tüm yorumları getirmek için.
    // List<Comment> findByTweetId(Long tweetId);
}