package com.workintech.s19c.controller;


import com.workintech.s19c.dto.RetweetDto;
import com.workintech.s19c.service.RetweetService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/retweet")
public class RetweetController {

    private final RetweetService retweetService;

    public RetweetController(RetweetService retweetService) {
        this.retweetService = retweetService;
    }

    // http://localhost:3000/retweet/[POST]
    @PostMapping
    public ResponseEntity<RetweetDto> retweetTweet(@RequestParam Long userId, @RequestParam Long tweetId) {

        RetweetDto newRetweet = retweetService.retweetTweet(userId, tweetId);
        return ResponseEntity.ok(newRetweet);

    }

    // http://localhost:3000/retweet/:id[DELETE]
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRetweet(@PathVariable("id") Long retweetId) {
        retweetService.deleteRetweet(retweetId);
        return ResponseEntity.noContent().build();
    }
}