package enerhi.boardservice.service;

import enerhi.boardservice.domain.PostStatus;
import enerhi.boardservice.domain.entity.Posts;
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

    /**
     * 글 쓰기
     */
    public Long save(Posts post) {
        post.setPostDate(dateTime());
        post.setStatus(PostStatus.INCLUDE);
        post.setViews(0);
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
        findPost.setPostEditDate(dateTime());
    }

    public String dateTime() {
        LocalDateTime localDateTime = LocalDateTime.now();
        String parsedLocalDateTimeNow = localDateTime.format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss"));
        return parsedLocalDateTimeNow;
    }

    /**
     * 글 삭제
     */
    public void postDelete(Long postId) {
        Posts findPost = postsRepository.findOne(postId);
        findPost.setPostDeleteDate(dateTime());
        findPost.setStatus(PostStatus.DELETE);
    }

    /**
     * 조회수 증가
     */
    public void postRead(Long postId, HttpServletRequest request, HttpServletResponse response) {
        // 쿠키 체크
        Cookie[] cookies = request.getCookies();
        boolean isVisited = false;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (("postView_"+ postId).equals(cookie.getName())) {
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
            Cookie cookie = new Cookie(("postView_"+postId), "true");
            cookie.setMaxAge(20); // 쿠키 유효 기간 설정 (초 단위)
            cookie.setPath("/"); // 쿠키의 유효 범위 설정
            response.addCookie(cookie); // 쿠키 추가
        }
    }

    public void postViews(Long postId) {
        Posts findPost = postsRepository.findOne(postId);
        findPost.setViews(findPost.getViews()+1);
    }

    /**
     * 글 검색
     */
    public List<Posts> postSearch(String type, String keyward) {
        if (type.equals("name")) {
            return postsRepository.findName(keyward);
        } else {
            return postsRepository.findTitle(keyward);
        }
    }

    public List<Posts> postSearch(String type, String keyward, int nowPage, int pagePostNumber) {
        if (type.equals("name")) {
            return postsRepository.findName(keyward, nowPage, pagePostNumber);
        } else {
            return postsRepository.findTitle(keyward, nowPage, pagePostNumber);
        }
    }

    /**
     * 페이징
     */
    public List<Posts> postPage(int nowPage, int pagePostNumber) {
        return postsRepository.page(nowPage, pagePostNumber);
    }
}
