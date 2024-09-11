package enerhi.boardservice.service;

import enerhi.boardservice.domain.PostStatus;
import enerhi.boardservice.domain.Posts;
import enerhi.boardservice.repository.PostsRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
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
        assertThat(post).isEqualTo(postsService.findOne(saveId));
    }

    @Test
    public void 글수정() throws Exception {
        //given
        Posts post = new Posts();
        post.setName("정관우");
        post.setTitle("안녕하세요.");
        post.setContent("잘 부탁드립니다.");
        Long saveId = postsService.save(post);

        //when
        postsService.postUpdate(saveId, "정관우2", "안녕하십니까?", "잘 부탁드리겠습니다." );

        //then
        assertThat(post.getName()).isEqualTo("정관우2"); //이름 수정 테스트
        assertThat(post.getTitle()).isEqualTo("안녕하십니까?"); //제목 수정 테스트
        assertThat(post.getContent()).isEqualTo("잘 부탁드리겠습니다."); //내용 수정 테스트
    }

    @Test
    public void 글삭제() throws Exception {
        //given
        Posts post = new Posts();
        post.setName("정관우");
        post.setStatus(PostStatus.INCLUDE); //글 생성시 기본값 INCLUDE
        Long saveId = postsService.save(post);

        //when
        postsService.postDelete(saveId); //포스트 삭제

        //then
        assertThat(post.getStatus()).isEqualTo(PostStatus.DELETE); //삭제 된 경우 Status = DELETE 테스트
    }
}
