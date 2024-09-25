package enerhi.boardservice.controller;

import enerhi.boardservice.domain.*;
import enerhi.boardservice.service.PostsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class PostController {
    private final PostsService postsService;

    //글 작성
    @GetMapping("/board/write")
    public String postWriteForm(Model model) {
        model.addAttribute("form", new PostForm());
        return "board/write";
    }

    @PostMapping("/board/write")
    public String postWrite(PostForm postForm) {
        Posts post = new Posts();
        post.setId(postForm.getId());
        post.setName(postForm.getName());
        post.setTitle(postForm.getTitle());
        post.setContent(postForm.getContent());
        post.setViews(0);

        postsService.save(post);

        return "redirect:/board/list";
    }

    SearchStatus searchStatus = SearchStatus.TITLE;
    //글 목록 조회
    @GetMapping("/board/list")
    public String postList(Model model,
                           @RequestParam(value = "type", required = false, defaultValue = "title") String type,
                           @RequestParam(value = "keyward", required = false) String keyward) {

        log.info("검색 유형 : " + type);
        log.info("검색 키워드 : " + keyward);

        if (keyward != null) {
            if (type.equals("title")) {
                searchStatus = SearchStatus.TITLE;
                List<Posts> posts = postsService.postSearch(type, keyward);
                model.addAttribute("posts", posts);
                model.addAttribute("keyward", keyward);
                model.addAttribute("searchStatus", searchStatus);
            } else {
                searchStatus = SearchStatus.NAME;
                List<Posts> posts = postsService.postSearch(type, keyward);
                model.addAttribute("posts", posts);
                model.addAttribute("keyward", keyward);
                model.addAttribute("searchStatus", searchStatus);
            }
        } else {
            List<Posts> posts = postsService.findPosts();
            model.addAttribute("posts", posts);
            model.addAttribute("keyward", null);
            model.addAttribute("searchStatus", searchStatus);
        }

        return "board/list";
    }


    //글 수정
    @GetMapping("/board/{postId}/edit")
    public String postUpdateForm(@PathVariable("postId") Long postId, Model model) {
        Posts post = postsService.findOne(postId);

        PostForm postForm = new PostForm();
        postForm.setId(post.getId());
        postForm.setName(post.getName());
        postForm.setTitle(post.getTitle());
        postForm.setContent(post.getContent());

        model.addAttribute("form", postForm);
        return "board/update";
    }

    @PostMapping("/board/{postId}/edit")
    public String postUpdate(@PathVariable("postId") Long postId, @ModelAttribute("form") PostForm postForm) {
        log.info("postUpdate 실행은 됐음!");
        postsService.postUpdate(postId, postForm.getName(), postForm.getTitle(), postForm.getContent());

        return "redirect:/board/list";
    }

    //글 삭제
    @PostMapping("/board/{postId}/delete")
    public String postDelete(@PathVariable("postId") Long postId) {
        log.info("삭제 함수 호출됨");
        postsService.postDelete(postId);

        return "redirect:/board/list";
    }

    //글 내용 조회
    @GetMapping("/board/{postId}/post")
    public String postShow(@PathVariable("postId") Long postId, Model model, HttpServletRequest request, HttpServletResponse response,
                           @RequestParam(value = "type", required = false, defaultValue = "title") String type,
                           @RequestParam(value = "keyward", required = false) String keyward) {
        Posts post = postsService.findOne(postId);
        postsService.postRead(postId, request, response);

        PostForm postForm = new PostForm();
        postForm.setId(post.getId());
        postForm.setName(post.getName());
        postForm.setTitle(post.getTitle());
        postForm.setContent(post.getContent());
        postForm.setPostDate(post.getPostDate());
        postForm.setViews(post.getViews());

        model.addAttribute("post",postForm);
        if (keyward != null) {
            if (type.equals("title")) {
                searchStatus = SearchStatus.TITLE;
                List<Posts> posts = postsService.postSearch(type, keyward);
                model.addAttribute("posts", posts);
                model.addAttribute("keyward", keyward);
                model.addAttribute("searchStatus", searchStatus);
            } else {
                searchStatus = SearchStatus.NAME;
                List<Posts> posts = postsService.postSearch(type, keyward);
                model.addAttribute("posts", posts);
                model.addAttribute("keyward", keyward);
                model.addAttribute("searchStatus", searchStatus);
            }
        } else {
            List<Posts> posts = postsService.findPosts();
            model.addAttribute("posts", posts);
            model.addAttribute("keyward", null);
            model.addAttribute("searchStatus", searchStatus);
        }

        model.addAttribute("form", postForm);
        return "/board/show";
    }
    }
