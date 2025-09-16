package com.workintech.s19c.service;


import com.workintech.s19c.dto.TweetDto;
import com.workintech.s19c.dto.UserDto;
import com.workintech.s19c.entity.Tweet;
import com.workintech.s19c.entity.User;
import com.workintech.s19c.repository.TweetRepository;
import com.workintech.s19c.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TweetService {

    private final TweetRepository tweetRepository;
    private final UserRepository userRepository;

    public TweetService(TweetRepository tweetRepository, UserRepository userRepository) {
        this.tweetRepository = tweetRepository;
        this.userRepository = userRepository;
    }

    // CREATE (POST)
    public TweetDto createTweet(Long userId, Tweet tweet) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found with id: " + userId);
        }

        User user = userOptional.get();
        tweet.setUser(user);
        tweet.setCreationDate(LocalDateTime.now());
        Tweet savedTweet = tweetRepository.save(tweet);
        return convertToDto(savedTweet);
    }

    // READ (GET) - Tüm tweet'ler için
    public List<TweetDto> getAllTweets() {
        return tweetRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // READ (GET) - Kullanıcı ID'sine göre
    public List<TweetDto> getTweetsByUserId(Long userId) {
        return tweetRepository.findByUserId(userId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // READ (GET) - Tweet ID'sine göre
    public Optional<TweetDto> getTweetById(Long tweetId) {
        return tweetRepository.findById(tweetId)
                .map(this::convertToDto);
    }

    // UPDATE (PUT)
    public TweetDto updateTweet(Long tweetId, String newContent) {
        Tweet existingTweet = tweetRepository.findById(tweetId)
                .orElseThrow(() -> new RuntimeException("Tweet not found with id: " + tweetId));

        // TODO: Güvenlik kontrolü ekle - sadece tweet sahibi güncelleyebilmeli.
        existingTweet.setContent(newContent);
        Tweet updatedTweet = tweetRepository.save(existingTweet);
        return convertToDto(updatedTweet);
    }

    // DELETE
    public void deleteTweet(Long tweetId) {
        // TODO: Güvenlik kontrolü ekle - sadece tweet sahibi silebilmeli.
        tweetRepository.deleteById(tweetId);
    }


    private TweetDto convertToDto(Tweet tweet) {
        TweetDto tweetDto = new TweetDto();
        tweetDto.setId(tweet.getId());
        tweetDto.setContent(tweet.getContent());
        tweetDto.setCreationDate(tweet.getCreationDate());

        UserDto userDto = new UserDto();
        userDto.setId(tweet.getUser().getId());
        userDto.setUsername(tweet.getUser().getUsername());

        tweetDto.setUser(userDto);
        return tweetDto;
    }
}
