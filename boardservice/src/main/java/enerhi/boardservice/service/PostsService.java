package enerhi.boardservice.service;

import enerhi.boardservice.domain.PostStatus;
import enerhi.boardservice.domain.Posts;
import enerhi.boardservice.repository.PostsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Transactional
public class PostsService {

    @Autowired
    PostsRepository postsRepository;

    LocalDateTime now = LocalDateTime.now();
    DateTimeFormatter dtFmt = DateTimeFormatter.ofPattern("yyyy.MM.dd hh:mm:ss");
    String ldtStr = now.format(dtFmt);

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
        findPost.setPostEditDate(ldtStr);
    }

    /**
     * 글 삭제
     */
    public void postDelete(Long postId) {
        Posts findPost = postsRepository.findOne(postId);
        findPost.setPostDeleteDate(ldtStr);
        findPost.setStatus(PostStatus.DELETE);
    }
}
