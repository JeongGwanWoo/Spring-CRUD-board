package enerhi.boardservice.domain;

import enerhi.boardservice.service.PostsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestDB {

    @Autowired
    private PostsService postsService;

    @Test
    void TestDataBase() {

        for (int i = 0; i < 1000; i++) {
            Posts post = new Posts();
            post.setName("관우_" + (i + 1));
            post.setTitle("테스트 " + (i + 1) + "번 입니다.");
            post.setContent("테스트 내용 " + (i + 1) + "번 입니다.");

            postsService.save(post);
        }


    }
}
