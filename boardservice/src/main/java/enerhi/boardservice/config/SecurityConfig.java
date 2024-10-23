package enerhi.boardservice.config;

import enerhi.boardservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity // 스프링 시큐리티가 스프링 필터 체인에 등록
public class SecurityConfig {

    @Autowired
    private final CorsConfig corsConfig;
    @Autowired
    private final UserRepository userRepository;
}
