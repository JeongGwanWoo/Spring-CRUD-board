package enerhi.boardservice.service;

import enerhi.boardservice.repository.UserRepository;
import enerhi.boardservice.security.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {


    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * 회원가입 중복 검증
     */
    public boolean signup_duplicate(User user) {

        User username = userRepository.findByUsername(user.getUsername());

        if (username == null) {
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            user.setRoles("ROLE_USER");
            userRepository.save(user);
            System.out.println("회원가입 성공");

            return true;
        } else {
            return false;
        }
    }
}
