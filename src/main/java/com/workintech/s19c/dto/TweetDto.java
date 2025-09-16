package com.workintech.s19c.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TweetDto {
    private Long id;
    private String content;
    private LocalDateTime creationDate;
    private UserDto user; // İlişkili kullanıcı bilgileri için DTO kullanıyoruz
}
