package com.workintech.s19c.service;


import com.workintech.s19c.dto.LikeDto;
import com.workintech.s19c.entity.Like;
import com.workintech.s19c.entity.Tweet;
import com.workintech.s19c.entity.User;
import com.workintech.s19c.repository.LikeRepository;
import com.workintech.s19c.repository.TweetRepository;
import com.workintech.s19c.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LikeService {

    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final TweetRepository tweetRepository;

    public LikeService(LikeRepository likeRepository, UserRepository userRepository, TweetRepository tweetRepository) {
        this.likeRepository = likeRepository;
        this.userRepository = userRepository;
        this.tweetRepository = tweetRepository;
    }

    public LikeDto likeTweet(Long userId, Long tweetId) {
        // Kullanıcı ve Tweet'in varlığını kontrol et
        Optional<User> userOptional = userRepository.findById(userId);
        Optional<Tweet> tweetOptional = tweetRepository.findById(tweetId);

        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found with id: " + userId);
        }
        if (tweetOptional.isEmpty()) {
            throw new RuntimeException("Tweet not found with id: " + tweetId);
        }

        // Kullanıcının bu tweet'i daha önce beğenip beğenmediğini kontrol et
        Optional<Like> existingLike = likeRepository.findByUserIdAndTweetId(userId, tweetId);
        if (existingLike.isPresent()) {
            // Zaten beğenilmişse hata döndür
            throw new IllegalStateException("User has already liked this tweet.");
        }

        Like like = new Like();
        like.setUser(userOptional.get());
        like.setTweet(tweetOptional.get());

        Like savedLike = likeRepository.save(like);
        return convertToDto(savedLike);
    }

    public void dislikeTweet(Long userId, Long tweetId) {
        // İlgili beğeniyi bul ve sil
        Optional<Like> existingLike = likeRepository.findByUserIdAndTweetId(userId, tweetId);

        if (existingLike.isEmpty()) {
            throw new RuntimeException("Like not found for this user and tweet.");
        }

        likeRepository.delete(existingLike.get());
    }

    private LikeDto convertToDto(Like like) {
        LikeDto likeDto = new LikeDto();
        likeDto.setId(like.getId());
        likeDto.setUserId(like.getUser().getId());
        likeDto.setTweetId(like.getTweet().getId());
        return likeDto;
    }
}