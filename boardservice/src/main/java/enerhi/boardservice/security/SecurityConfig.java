package enerhi.boardservice.security;

import enerhi.boardservice.security.auth.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity // 스프링 시큐리티가 스프링 필터 체인에 등록
public class SecurityConfig {

    @Autowired
    private final CorsConfig corsConfig;

    @Bean // authenticationManager 를 IoC에 등록해준다.
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable);
        http
                .sessionManagement(sc -> sc.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션을 사용하지 않음
                .addFilter(corsConfig.corsFilter()) // @CorssOrigin(인증X), 시큐리티 필터에 등록 인증O --> 모든 요청 허용.
//                .addFilter(new JwtAuthenticationFilter(authenticationManager))
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("board/user/**").authenticated() // /board/user으로 들어오면 인증이 필요하다.
                        .requestMatchers("board/admin/**").hasRole("ADMIN") // /board/admin으로 들어오면 ADMIN 권한이 있는 사람만 접근 가능하다.
                        .anyRequest().permitAll() // 그리고 나머지 url은 전부 허용해준다.
                );
        return http.build();
    }
}
