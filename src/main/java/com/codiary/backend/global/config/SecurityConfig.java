package com.codiary.backend.global.config;

import com.codiary.backend.global.jwt.EmailPasswordAuthenticationFilter;
import com.codiary.backend.global.jwt.JwtAuthenticationFilter;
import com.codiary.backend.global.jwt.JwtTokenProvider;
import com.codiary.backend.global.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtTokenProvider jwtTokenProvider;
    private final TokenRepository tokenRepository;

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
                .csrf(csrf->csrf.disable())
                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(
                        authorize -> authorize
                                // Member 관련 접근
                                .requestMatchers("/members/sign-up", "/members/sign-up/check-email", "/members/sign-up/check-nickname").permitAll()
                                .requestMatchers("/members/login", "/members/logout", "/members/posts", "/members/info").permitAll()
                                .requestMatchers("/members/**").permitAll()
                                .requestMatchers("oauth/login/**").permitAll()
                                // Post 관련 접근
                                .requestMatchers("/posts","/posts/{postId}", "/posts/visibility/{postId}", "/posts/coauthors/{postId}", "/posts/categories/{postId}").permitAll()
                                .requestMatchers("/posts/title/paging", "/posts/team/{teamId}/{postId}", "/posts/team/{teamId}/member/{memberId}/paging", "/posts/project/{projectId}/team/{teamId}/paging").permitAll()
                                .requestMatchers("/posts/project/{projectId}/member/{memberId}/paging", "/posts/member/{memberId}/paging", "/posts/categories/paging", "/posts/{postId}/adjacent").permitAll()
                                .requestMatchers("/posts/poplular/list", "/posts/latest/list", "/posts/comments/list/{postId}", "/posts/search/title/body/member/project/categories").permitAll()
                                // Comment 관련 접근
                                .requestMatchers("/comments/count/{postId}", "/comments/delete/{commentId}", "/comments/patch/{commentId}").permitAll()
                                // Team 관련 접근
                                .requestMatchers("/teams", "/teams/{teamId}", "/teams/{teamId}/project", "/teams/follow/{teamId}", "/teams/{teamId}/profileImage").permitAll()
                                .requestMatchers("/teams/{teamId}/bannerImage", "/teams/profile/{teamId}", "/teams/{teamId}/followers", "/teams/list").permitAll()
                                // Bookmark 관련 접근
                                .requestMatchers("/bookmarks/add/{memberId}/{postId}", "/bookmarks/count/{postId}", "/bookmarks/delete/{bookmarkId}").permitAll()
                                // Calendar 관련 접근
                                .requestMatchers("/calendar", "/calendar/project").permitAll()
                                // Project 관련 접근
                                .requestMatchers("/projects/list").permitAll()
                                // Category 관련 접근
                                .requestMatchers("/categories/list").permitAll()
                                // 기타 관련 접근
                                .requestMatchers("/", "/api-docs/**", "/api-docs/swagger-config/*", "/swagger-ui/*", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                                .anyRequest().authenticated()
                )
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider, tokenRepository), EmailPasswordAuthenticationFilter.class).build();
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
        config.setAllowedOriginPatterns(List.of("*", "http://localhost:3000, "));
        config.setAllowedOrigins(List.of("http://localhost:3000"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(List.of("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
