package enerhi.boardservice.service;

import enerhi.boardservice.domain.Posts;
import enerhi.boardservice.repository.PostsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class PostsService {

    @Autowired
    PostsRepository postsRepository;

    /**
     * 글 쓰기
     */
    @Transactional
    public Long save(Posts post) {
        postsRepository.save(post);
        return post.getId();
    }

    /**
     * 글 조회
     */
    public Posts findOne(Long postId) {
        return postsRepository.findOne(postId);
    }

    public List<Posts> postsList() {
        return postsRepository.findAll();
    }
}
