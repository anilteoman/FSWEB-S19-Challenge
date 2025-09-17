package com.workintech.s19c.service;


import com.workintech.s19c.dto.RetweetDto;
import com.workintech.s19c.entity.Retweet;
import com.workintech.s19c.entity.Tweet;
import com.workintech.s19c.entity.User;
import com.workintech.s19c.repository.RetweetRepository;
import com.workintech.s19c.repository.TweetRepository;
import com.workintech.s19c.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RetweetService {

    private final RetweetRepository retweetRepository;
    private final UserRepository userRepository;
    private final TweetRepository tweetRepository;

    public RetweetService(RetweetRepository retweetRepository, UserRepository userRepository, TweetRepository tweetRepository) {
        this.retweetRepository = retweetRepository;
        this.userRepository = userRepository;
        this.tweetRepository = tweetRepository;
    }

    public RetweetDto retweetTweet(Long userId, Long tweetId) {
        // Kullanıcı ve Tweet'in varlığını kontrol et
        Optional<User> userOptional = userRepository.findById(userId);
        Optional<Tweet> tweetOptional = tweetRepository.findById(tweetId);

        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found with id: " + userId);
        }
        if (tweetOptional.isEmpty()) {
            throw new RuntimeException("Tweet not found with id: " + tweetId);
        }

        // Kullanıcının bu tweet'i daha önce retweet edip etmediğini kontrol et
        Optional<Retweet> existingRetweet = retweetRepository.findByUserIdAndTweetId(userId, tweetId);
        if (existingRetweet.isPresent()) {
            // Zaten retweet edilmişse hata döndür
            throw new IllegalStateException("User has already retweeted this tweet.");
        }

        Retweet retweet = new Retweet();
        retweet.setUser(userOptional.get());
        retweet.setTweet(tweetOptional.get());

        Retweet savedRetweet = retweetRepository.save(retweet);
        return convertToDto(savedRetweet);
    }

    public void deleteRetweet(Long retweetId) {
        retweetRepository.deleteById(retweetId);
    }

    private RetweetDto convertToDto(Retweet retweet) {
        RetweetDto retweetDto = new RetweetDto();
        retweetDto.setId(retweet.getId());
        retweetDto.setUserId(retweet.getUser().getId());
        retweetDto.setTweetId(retweet.getTweet().getId());
        return retweetDto;
    }
}
