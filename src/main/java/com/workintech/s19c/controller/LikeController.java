package com.workintech.s19c.controller;


import com.workintech.s19c.dto.LikeDto;
import com.workintech.s19c.service.LikeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class LikeController {

    private final LikeService likeService;

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    // http://localhost:9000/like/[POST]
    @PostMapping("/like")
    public ResponseEntity<LikeDto> likeTweet(@RequestParam Long userId, @RequestParam Long tweetId) {

        LikeDto newLike = likeService.likeTweet(userId, tweetId);
        return ResponseEntity.ok(newLike);

    }

    // http://localhost:9000/dislike/[POST]
    @PostMapping("/dislike")
    public ResponseEntity<Void> dislikeTweet(@RequestParam Long userId, @RequestParam Long tweetId) {

        likeService.dislikeTweet(userId, tweetId);
        return ResponseEntity.noContent().build();

    }
}