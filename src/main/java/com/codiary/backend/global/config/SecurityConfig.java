package com.codiary.backend.global.config;

import com.codiary.backend.global.jwt.EmailPasswordAuthenticationFilter;
import com.codiary.backend.global.jwt.JwtAuthenticationFilter;
import com.codiary.backend.global.jwt.JwtTokenProvider;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .formLogin(AbstractHttpConfigurer::disable)
//                .httpBasic(AbstractHttpConfigurer::disable)
//                .csrf(AbstractHttpConfigurer::disable)
//                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
//                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .authorizeHttpRequests(authorize -> authorize
//                        .requestMatchers("/members/sign-up").permitAll()
//                        .requestMatchers("/members/login").permitAll()
//                        .requestMatchers("/posts").permitAll()//hasRole("USER")
//                        .requestMatchers("/", "/api-docs/**", "/api-docs/swagger-config/*", "/swagger-ui/*", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
//                        .anyRequest().authenticated()
//                )
//                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);
//
//        return http.build();
        return http
                .httpBasic(httpBasic -> httpBasic.disable())
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(
                        authorize -> authorize
                                // Member 관련 접근
                                .requestMatchers("/api/v2/auth/**").permitAll()
                                .requestMatchers("/api/v2/oauth/**").permitAll()
                                // Post 관련 접근
                                .requestMatchers("/api/v2/post/search").permitAll()
                                .requestMatchers("/api/v2/posts","/api/v2/posts/{postId}", "/api/v2/posts/visibility/{postId}", "/api/v2/posts/coauthors/{postId}", "/api/v2/posts/categories/{postId}").permitAll()
                                .requestMatchers("/api/v2/posts/title/paging", "/api/v2/posts/team/{teamId}/{postId}", "/api/v2/posts/team/{teamId}/member/{memberId}/paging", "/api/v2/posts/project/{projectId}/team/{teamId}/paging").permitAll()
                                .requestMatchers("/api/v2/posts/project/{projectId}/member/{memberId}/paging", "/api/v2/posts/member/{memberId}/paging", "/api/v2/posts/categories/paging", "/api/v2/posts/{postId}/adjacent").permitAll()
                                .requestMatchers("/api/v2/posts/poplular/list", "/api/v2/posts/latest/list", "/api/v2/posts/comments/list/{postId}", "/api/v2/posts/search/title/body/member/project/categories").permitAll()

                                // Comment 관련 접근
                                .requestMatchers("/api/v2/post/{post_id}/comment").permitAll() // 게시물에 댓글 생성, 조회
                                .requestMatchers("/api/v2/comment/{comment_id}/reply").permitAll() // 댓글에 대댓글 생성, 조회
                                .requestMatchers("/api/v2/comment/{comment_id}").permitAll() // 댓글 & 대댓글 수정 삭제

                                // Team 관련 접근
                                .requestMatchers("/api/v2/teams/team_member").permitAll()
                                .requestMatchers("/api/v2/teams/{team_id}/profile_image").permitAll()
                                .requestMatchers("/api/v2/teams/{team_id}/banner_image").permitAll()
                                // Bookmark 관련 접근
                                // Calendar 관련 접근
                                // Project 관련 접근
                                // Category 관련 접근
                                // 기타 관련 접근
                                .requestMatchers("/", "/api-docs/**", "/api-docs/swagger-config/*", "/swagger-ui/*", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                                .anyRequest().authenticated()
                )
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), EmailPasswordAuthenticationFilter.class).build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
        //return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }


    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOriginPatterns(List.of("*", "http://localhost:3000", "https://www.codiary.site"));
        config.setAllowedOrigins(List.of("http://localhost:3000", "https://www.codiary.site"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(List.of("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
