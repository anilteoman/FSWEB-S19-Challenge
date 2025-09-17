package com.workintech.s19c;


import com.workintech.s19c.dto.TweetDto;
import com.workintech.s19c.entity.Tweet;
import com.workintech.s19c.entity.User;
import com.workintech.s19c.repository.TweetRepository;
import com.workintech.s19c.repository.UserRepository;
import com.workintech.s19c.service.TweetService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class TweetServiceTest {

    @Mock
    private TweetRepository tweetRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TweetService tweetService;

    @Test
    void givenValidUserAndTweet_whenCreateTweet_thenTweetIsCreatedSuccessfully() {
        // GIVEN - Test için gerekli koşulları ayarla
        Long userId = 1L;
        Tweet tweetToCreate = new Tweet();
        tweetToCreate.setContent("Hello, world!");

        User mockUser = new User();
        mockUser.setId(userId);

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        Mockito.when(tweetRepository.save(ArgumentMatchers.any(Tweet.class))).thenReturn(tweetToCreate);

        // WHEN - Test edilecek metodu çağır
        TweetDto createdTweet = tweetService.createTweet(userId, tweetToCreate);

        // THEN - Sonuçları doğrula
        Assertions.assertNotNull(createdTweet);
        Assertions.assertEquals("Hello, world!", createdTweet.getContent());
        Assertions.assertNotNull(createdTweet.getUser());
        Assertions.assertEquals(userId, createdTweet.getUser().getId());

        // Metotların doğru sayıda çağrıldığını doğrula
        Mockito.verify(userRepository, Mockito.times(1)).findById(userId);
        Mockito.verify(tweetRepository, Mockito.times(1)).save(ArgumentMatchers.any(Tweet.class));
    }

    @Test
    void givenNonexistentUser_whenCreateTweet_thenThrowException() {
        // GIVEN
        Long userId = 999L;
        Tweet tweetToCreate = new Tweet();

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // WHEN & THEN
        Assertions.assertThrows(RuntimeException.class, () -> tweetService.createTweet(userId, tweetToCreate));

        // save metodunun çağrılmadığını doğrula
        Mockito.verify(tweetRepository, Mockito.never()).save(ArgumentMatchers.any(Tweet.class));
    }
}