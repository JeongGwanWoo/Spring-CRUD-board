package enerhi.boardservice.service;

import enerhi.boardservice.domain.PostStatus;
import enerhi.boardservice.domain.Posts;
import enerhi.boardservice.repository.PostsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class PostsService {

    @Autowired
    PostsRepository postsRepository;

    /**
     * 글 쓰기
     */
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

    public List<Posts> findPosts() {
        return postsRepository.findAll();
    }

    /**
     * 글 수정
     */
    public void postUpdate(Long postId, String name, String title, String content) {
        Posts findPost = postsRepository.findOne(postId);
        findPost.setName(name);
        findPost.setTitle(title);
        findPost.setContent(content);
    }

    /**
     * 글 삭제
     */
    public void postDelete(Long postId) {
        Posts findPost = postsRepository.findOne(postId);
        findPost.setPostDeleteDate(LocalDateTime.now());
        findPost.setStatus(PostStatus.DELETE);
    }
}
