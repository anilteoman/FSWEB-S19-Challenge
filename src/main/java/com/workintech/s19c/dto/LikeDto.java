package com.workintech.s19c.dto;


import lombok.Data;

@Data
public class LikeDto {
    private Long id;
    private Long userId;
    private Long tweetId;
}