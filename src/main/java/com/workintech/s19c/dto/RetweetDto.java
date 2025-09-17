package com.workintech.s19c.dto;


import lombok.Data;

@Data
public class RetweetDto {
    private Long id;
    private Long userId;
    private Long tweetId;
}