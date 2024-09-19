package enerhi.boardservice.service;

import enerhi.boardservice.domain.PostStatus;
import enerhi.boardservice.domain.Posts;
import enerhi.boardservice.repository.PostsRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
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

    /**
     * 조회수 증가
     */
    @Transactional
    public void postRead(Long postId, HttpServletRequest request, HttpServletResponse response) {
        // 쿠키 체크
        Cookie[] cookies = request.getCookies();
        boolean isVisited = false;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (("postView"+ postId).equals(cookie.getName())) {
                    log.info("["+ postId + "]"+"조회수 중복 발생!");
                    isVisited = true; // 쿠키가 존재하면 이미 방문한 것으로 간주
                    break;
                }
            }
        }

        // 방문한 적이 없으면 조회수 증가 로직 처리
        if (!isVisited) {
            postViews(postId); // 조회수 증가 함수 호출

            // 방문 기록을 위한 쿠키 생성
            Cookie cookie = new Cookie(("postView"+postId), "true");
            cookie.setMaxAge(60 * 60 * 24); // 쿠키 유효 기간 설정 (초 단위)
            cookie.setPath("/"); // 쿠키의 유효 범위 설정
            response.addCookie(cookie); // 쿠키 추가
        }
    }

    public void postViews(Long postId) {
        Posts findPost = postsRepository.findOne(postId);
        findPost.setViews(findPost.getViews()+1);
    }
}
