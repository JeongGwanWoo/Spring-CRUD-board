package enerhi.boardservice.controller;

import enerhi.boardservice.domain.PostForm;
import enerhi.boardservice.domain.Posts;
import enerhi.boardservice.repository.PostsRepository;
import enerhi.boardservice.service.PostsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class PostController {
    private final PostsService postsService;

    //글 작성
    @GetMapping("/board/write")
    public String postWrite(Model model) {
        model.addAttribute("form", new PostForm());
        return "board/write";
    }

    //글 목록 조회
    @GetMapping("/board/list")
    public String postList(Model model) {
        List<Posts> posts = postsService.findPosts();
        model.addAttribute("posts", posts);
        return "board/list";
    }
}
