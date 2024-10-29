package enerhi.boardservice.controller;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import enerhi.boardservice.domain.*;
import enerhi.boardservice.repository.UserRepository;
import enerhi.boardservice.security.auth.PrincipalDetails;
import enerhi.boardservice.security.auth.jwt.JwtProperties;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Date;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManager authenticationManager;

    @PostMapping("signup")
    public ResponseEntity<?> signup(@RequestBody User user) {
        try {
            System.out.println("회원가입 중");
            System.out.println("user = " + user);

            if (user != null) {
                user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
                user.setRoles("ROLE_USER");
                userRepository.save(user);
                return ResponseEntity.ok(new SignupResponse(true, "회원가입 성공"));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new SignupResponse(false, "회원가입 실패"));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new SignupResponse(false, "회원가입 중 에러 발생"));
        }
    }

    @PostMapping("login")
    public ResponseEntity<LoginResponse> login(@RequestBody UserLoginRequest userLoginRequest){
        System.out.println("POST login 호출 ---로그인중---");
        try {
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(userLoginRequest.getUsername(), userLoginRequest.getPassword());

            // PrincipalDetailsService의 loadUserByUsername() 함수가 실행된 후 정상이면 authentication이 리턴됨.
            Authentication authentication = authenticationManager.authenticate(authenticationToken);

            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
            System.out.println("로그인 완료됨: " + principalDetails.getUser().getUsername());

            String jwtToken = createJwtToken(authentication);
            LoginResponse loginResponse = new LoginResponse(true, jwtToken);

            return ResponseEntity.ok(loginResponse);
        } catch (AuthenticationException e) {
            System.out.println("POST login 호출 ---예외 발생---");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new LoginResponse(false, "Invalid credentials"));
        }
    }

    private String createJwtToken(Authentication authentication) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        return JWT.create()
                .withSubject(principalDetails.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + (JwtProperties.EXPIRATION_TIME)))
                .withClaim("id", principalDetails.getUser().getId())
                .withClaim("username", principalDetails.getUser().getUsername())
                .sign(Algorithm.HMAC512(JwtProperties.SECRET));
    }

    @GetMapping("/board/user/info")
    public ResponseEntity<?> userProfile(Authentication authentication) {
        System.out.println("사용자 정보 호출 완료");
        // 로그인한 사용자 정보 가져오기 (PrincipalDetails 같은 인증 객체에서 가져올 수 있음)
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

        // 사용자 정보 생성
        UserProfile profile = new UserProfile();

        profile.setUsername(principalDetails.getUsername());

        return ResponseEntity.ok(profile);
    }

    @GetMapping("board/user")
    public String user() {

        return "유저페이지";
    }

    @GetMapping("board/admin")
    public String admin() {

        return "어드민페이지";
    }

}
