package com.workintech.s19c;


import com.fasterxml.jackson.databind.ObjectMapper;

import com.workintech.s19c.dto.LoginRequest;
import com.workintech.s19c.entity.Tweet;
import com.workintech.s19c.entity.User;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class TwitterIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    private static String JWT_TOKEN;
    private static Long USER_ID;
    private static Long TWEET_ID;
    private static Long COMMENT_ID;
    private static Long LIKE_ID;
    private static Long RETWEET_ID;

    private String getAuthHeader() {
        return "Bearer " + JWT_TOKEN;
    }

    @Nested
    class UserTests {
        @Test
        void canRegisterAndLoginUser() throws Exception {
            // Register
            User user = new User();
            user.setUsername("testuser_int");
            user.setPassword("password123");
            user.setEmail("test_int@test.com");

            mockMvc.perform(post("/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(user)))
                    .andExpect(status().isOk());

            // Login
            LoginRequest loginRequest = new LoginRequest();
            loginRequest.setUsername("testuser_int");
            loginRequest.setPassword("password123");

            MvcResult result = mockMvc.perform(post("/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(loginRequest)))
                    .andExpect(status().isOk())
                    .andReturn();

            JWT_TOKEN = result.getResponse().getContentAsString();
            System.out.println("JWT Token: " + JWT_TOKEN);
        }
    }

    @Nested
    class TweetTests {
        // Ön koşul: Kullanıcı ve JWT token'ı oluşturulmuş olmalı
        void setup() throws Exception {
            // Önce kullanıcı oluşturup giriş yap
            TwitterIntegrationTest.this.canRegisterAndLoginUser();
            // ID'yi al
            MvcResult result = mockMvc.perform(get("/user/findByUsername?username=testuser_int"))
                    .andExpect(status().isOk())
                    .andReturn();
            USER_ID = objectMapper.readTree(result.getResponse().getContentAsString()).get("id").asLong();
        }

        @Test
        void canCreateAndRetrieveTweet() throws Exception {
            setup();
            // Create
            Tweet tweet = new Tweet();
            tweet.setContent("This is an integration test tweet!");

            MvcResult createResult = mockMvc.perform(post("/tweet")
                            .param("userId", String.valueOf(USER_ID))
                            .header("Authorization", getAuthHeader())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(tweet)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content").value("This is an integration test tweet!"))
                    .andReturn();

            TWEET_ID = objectMapper.readTree(createResult.getResponse().getContentAsString()).get("id").asLong();

            // Retrieve by user ID
            mockMvc.perform(get("/tweet/findByUserId")
                            .param("userId", String.valueOf(USER_ID))
                            .header("Authorization", getAuthHeader()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].content").value("This is an integration test tweet!"));
        }

        @Test
        void canUpdateAndDeleteTweet() throws Exception {
            canCreateAndRetrieveTweet(); // Ön koşul: Tweet oluşturulmuş olmalı

            // Update
            Tweet updatedTweet = new Tweet();
            updatedTweet.setContent("Updated test tweet.");
            mockMvc.perform(put("/tweet/" + TWEET_ID)
                            .header("Authorization", getAuthHeader())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updatedTweet)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content").value("Updated test tweet."));

            // Delete
            mockMvc.perform(delete("/tweet/" + TWEET_ID)
                            .header("Authorization", getAuthHeader()))
                    .andExpect(status().isNoContent());

            // Verify deletion
            mockMvc.perform(get("/tweet/" + TWEET_ID)
                            .header("Authorization", getAuthHeader()))
                    .andExpect(status().isNotFound());
        }
    }
}