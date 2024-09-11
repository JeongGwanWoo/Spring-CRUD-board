package enerhi.boardservice;

import enerhi.boardservice.domain.Posts;
import enerhi.boardservice.service.PostsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HelloController {

    private final PostsService postsService;

    @GetMapping("hello")
    public String hello(Model model) {
        model.addAttribute("data", "hello!");
        return "posts/postList";
    }
}
