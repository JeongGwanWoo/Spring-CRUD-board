package enerhi.boardservice.service;

import enerhi.boardservice.security.domain.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    UserService userService;

    @Test
    public void signup_duplicate() throws Exception {
        //given
        User user = new User();
        user.setUsername("jkw4805"); // 기존에 회원과 아이디를 중복되게 설정

        //when
        boolean b = userService.signup_duplicate(user); // 회원가입 성공 시 true, 실패 시 false

        //then
        assertThat(b).isEqualTo(false);
    }
}