package enerhi.boardservice.controller;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import enerhi.boardservice.domain.dto.PostForm;
import enerhi.boardservice.domain.entity.Posts;
import enerhi.boardservice.security.domain.dto.*;
import enerhi.boardservice.security.domain.entity.User;
import enerhi.boardservice.repository.UserRepository;
import enerhi.boardservice.security.auth.PrincipalDetails;
import enerhi.boardservice.security.auth.jwt.JwtProperties;
import enerhi.boardservice.service.PostsService;
import enerhi.boardservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final PostsService postsService;


    // 회원가입
    @PostMapping("signup")
    public ResponseEntity<?> signup(@RequestBody User user) {
        try {
            System.out.println("회원가입 중");
            System.out.println("user = " + user);

            if (userService.signup_duplicate(user)) {
                return ResponseEntity.ok(new SignupResponse(true, "회원가입 성공"));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new SignupResponse(false, "중복된 아이디입니다."));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new SignupResponse(false, "회원가입 중 에러 발생"));
        }
    }

    // 로그인
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

    // 토큰 생성
    private String createJwtToken(Authentication authentication) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        return JWT.create()
                .withSubject(principalDetails.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + (JwtProperties.EXPIRATION_TIME)))
                .withClaim("id", principalDetails.getUser().getId())
                .withClaim("username", principalDetails.getUser().getUsername())
                .sign(Algorithm.HMAC512(JwtProperties.SECRET));
    }

    // 사용자 정보 호출
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

    // 글 작성
    @PostMapping("/board/user/write")
    public ResponseEntity<?> postWrite(@RequestBody PostForm postForm,
                            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        System.out.println("글 작성 호출");
        if (principalDetails != null) {
            String username = principalDetails.getUsername();
            Posts post = new Posts();
            post.setId(postForm.getId());
            post.setName(username);
            post.setTitle(postForm.getTitle());
            post.setContent(postForm.getContent());
            postsService.save(post);
        }
        return ResponseEntity.ok(true);
    }

    //글 삭제
    @PostMapping("/board/user/{postId}/delete")
    public ResponseEntity<?> postDelete(@PathVariable("postId") Long postId,
                             @AuthenticationPrincipal PrincipalDetails principalDetails) {
        System.out.println("삭제 호출");
        if (principalDetails != null) {
            postsService.postDelete(postId);
        }

        return ResponseEntity.ok(true);
    }

    // 글 수정
    @PostMapping("/board/user/{postId}/edit")
    public ResponseEntity<?> postUpdate(@PathVariable("postId") Long postId,
                                        @ModelAttribute("form") PostForm old_postForm,
                                        @RequestBody PostForm new_postForm,
                                        @AuthenticationPrincipal PrincipalDetails principalDetails) {
        System.out.println("수정 호출");
        if (principalDetails != null) {
            String username = principalDetails.getUsername();
            new_postForm.setName(username);
            postsService.postUpdate(postId, new_postForm.getName(), new_postForm.getTitle(), new_postForm.getContent());
        }
        return ResponseEntity.ok(true);
    }

//    // 글 작성
//    @GetMapping("/board/user/write")
//    public ResponseEntity<?> postWriteForm(Model model, @AuthenticationPrincipal PrincipalDetails principalDetails) {
//        System.out.println("principalDetails = " + principalDetails);
//        try {
//            if (principalDetails != null) {
//                System.out.println("principalDetails null 아님");
//                String username = principalDetails.getUsername();
//                model.addAttribute("username", username);
//                model.addAttribute("form", new PostForm());
//                return ResponseEntity.ok(new ResponseDto(true, "글 작성 페이지 이동"));
//            } else {
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDto(false, "글 작성 페이지 이동 실패"));
//            }
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDto(false, "글 작성 페이지 이동 중 오류 발생"));
//        }
//
//    }

    @GetMapping("board/user")
    public String user() {

        return "유저페이지";
    }

    @GetMapping("board/admin")
    public String admin() {

        return "어드민페이지";
    }

}
