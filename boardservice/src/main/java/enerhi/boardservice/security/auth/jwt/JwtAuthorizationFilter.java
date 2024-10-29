package enerhi.boardservice.security.auth.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import enerhi.boardservice.domain.User;
import enerhi.boardservice.repository.UserRepository;
import enerhi.boardservice.security.auth.PrincipalDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
    private UserRepository userRepository;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository) {
        super(authenticationManager);
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
//        super.doFilterInternal(request, response, chain);
        System.out.println("인증 또는 권한이 필요한 주소 요청");
        try {
            String jwtHeader = request.getHeader(JwtProperties.HEADER_STRING);

            // header 가 있는지 확인.
            if (jwtHeader == null || !jwtHeader.startsWith(JwtProperties.TOKEN_PREFIX.replace(" ", ""))) {
                System.out.println("jwtHeader = " + jwtHeader);
                chain.doFilter(request, response);
                return;
            }
            System.out.println("jwtHeader = " + jwtHeader);

            String jwtToken = jwtHeader.replace(JwtProperties.TOKEN_PREFIX, "");
            String username =
                    JWT.require(Algorithm.HMAC512(JwtProperties.SECRET))
                            .build()
                            .verify(jwtToken)
                            .getClaim("username")
                            .asString();

            System.out.println("jwtToken = " + jwtToken);

            // 서명 확인
            if (username != null) {
                User userEntity = userRepository.findByUsername(username);
                PrincipalDetails principalDetails = new PrincipalDetails(userEntity);

                // jwt 토큰 서명을 통해서 서명이 정상이면 Authentication 객체를 만든다. -> 로그인 인증된 이후라서 괜찮다.
                Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());

                // 강제로 시큐리티의 세션에 접근하여 Authentication 객체를 저장
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            chain.doFilter(request, response);
        } catch (TokenExpiredException e) {
            System.out.println("예외 발생: TokenExpiredException");
        } catch (JWTDecodeException e) {
            System.out.println("예외 발생: JWTDecodeException");
        }
    }
}
