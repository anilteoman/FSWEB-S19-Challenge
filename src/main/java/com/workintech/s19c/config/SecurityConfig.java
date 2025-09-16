package com.workintech.s19c.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // CSRF korumasını kapat (API için genelde kapatılır)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/register/**", "/login/**").permitAll() // Kayıt ve giriş endpointlerine erişimi herkese aç
                        .anyRequest().authenticated() // Diğer tüm isteklere sadece kimliği doğrulanmış kullanıcılar erişebilir
                );

        return http.build();
    }
}