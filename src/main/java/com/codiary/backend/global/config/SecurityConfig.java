package com.codiary.backend.global.config;

import com.codiary.backend.global.jwt.EmailPasswordAuthenticationFilter;
import com.codiary.backend.global.jwt.JwtAuthenticationFilter;
import com.codiary.backend.global.jwt.JwtTokenProvider;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
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
                .httpBasic(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(
                        authorize -> authorize
                                .requestMatchers("/error").permitAll()
                                // Member 관련 접근
                                .requestMatchers("/api/v2/auth/**").permitAll()
                                .requestMatchers("/api/v2/member/**").permitAll()
                                // Post 관련 접근
                                .requestMatchers("/api/v2/post/**").permitAll()
                                .requestMatchers("/api/v2/post/search").permitAll()
                                .requestMatchers("/api/v2/post","/api/v2/post/{postId}", "/api/v2/post/visibility/{postId}", "/api/v2/post/coauthor/{postId}", "/api/v2/post/category/{postId}").permitAll()
                                .requestMatchers("/api/v2/post/title/paging", "/api/v2/post/team/{teamId}/{postId}", "/api/v2/post/team/{teamId}/member/{memberId}/paging", "/api/v2/post/project/{projectId}/team/{teamId}/paging").permitAll()
                                .requestMatchers("/api/v2/post/project/{projectId}/member/{memberId}/paging", "/api/v2/post/member/{memberId}/paging", "/api/v2/post/category/paging", "/api/v2/post/{postId}/adjacent").permitAll()
                                .requestMatchers("/api/v2/post/comment/list/{postId}", "/api/v2/post/search/title/body/member/project/category").permitAll()
                                .requestMatchers("/api/v2/post/popular", "api/v2/post/popular/{category_id}").permitAll() // 전체 인기글 조회 & 관심 카테고리 인기글 조회
                                .requestMatchers("/api/v2/post/latest", "api/v2/post/following").permitAll() // 전체 최신글 조회 & 팔로잉 멤버들의 최신글 조회

                                // Comment 관련 접근
                                .requestMatchers("api/v2/comment/**").permitAll()
                                .requestMatchers("/api/v2/post/{post_id}/comment").permitAll() // 게시물에 댓글 생성, 조회
                                .requestMatchers("/api/v2/comment/{comment_id}/reply").permitAll() // 댓글에 대댓글 생성, 조회
                                .requestMatchers("/api/v2/comment/{comment_id}").permitAll() // 댓글 & 대댓글 수정 삭제

                                // Team 관련 접근
                                .requestMatchers("/api/v2/team/**").permitAll()
                                .requestMatchers("/api/v2/team/team_member").permitAll()
                                .requestMatchers("/api/v2/team/{team_id}/profile_image").permitAll()
                                .requestMatchers("/api/v2/team/{team_id}/banner_image").permitAll()

                                // follow 관련 접근
                                .requestMatchers("/api/v2/follow/**").permitAll()

                                // Project 관련 접근
                                .requestMatchers("/api/v2/project/**").permitAll()
                                // Category 관련 접근
                                .requestMatchers("/api/v2/category/**").permitAll()
                                // 알람 관련 접근
                                .requestMatchers("/api/v2/connect", "/api/v2/disconnect", "/api/v2/alert/**").permitAll()
                                // 기타 관련 접근
                                .requestMatchers("/**", "/api-docs/**", "/api-docs/swagger-config/*", "/swagger-ui/*", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
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
        config.setAllowedOriginPatterns(List.of("*", "http://localhost:3000", "https://www.codiary.site", "https://codiary.site", "https://api.codiary.site",
                "http://localhost:3000/", "https://www.codiary.site/", "https://codiary.site/", "https://api.codiary.site/"));
        config.setAllowedOrigins(List.of("*", "http://localhost:3000", "https://www.codiary.site", "https://codiary.site", "https://api.codiary.site",
                "http://localhost:3000/", "https://www.codiary.site/", "https://codiary.site/", "https://api.codiary.site/"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(List.of("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
