package com.workintech.s19c.repository;


import com.workintech.s19c.entity.Retweet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RetweetRepository extends JpaRepository<Retweet, Long> {
    // Belirli bir kullanıcı ve tweet'in retweet kaydını bulmak için
    Optional<Retweet> findByUserIdAndTweetId(Long userId, Long tweetId);
}
