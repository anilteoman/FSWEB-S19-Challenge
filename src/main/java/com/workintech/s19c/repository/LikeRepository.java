package com.workintech.s19c.repository;


import com.workintech.s19c.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    // Belirli bir kullanıcı ve tweet'in beğeni kaydını bulmak için
    Optional<Like> findByUserIdAndTweetId(Long userId, Long tweetId);
}
