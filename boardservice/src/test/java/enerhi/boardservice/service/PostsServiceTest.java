package enerhi.boardservice.service;

import enerhi.boardservice.domain.Posts;
import enerhi.boardservice.repository.PostsRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
public class PostsServiceTest {

    @Autowired PostsRepository postsRepository;
    @Autowired PostsService postsService;

    @Test
    public void 글쓰기() throws Exception {
        //given
        Posts post = new Posts();
        post.setName("정관우");

        //when
        Long saveId = postsService.save(post);

        //then
        assertEquals(post, postsRepository.findOne(saveId));
        assertEquals(post, postsService.findOne(saveId));
    }
}
