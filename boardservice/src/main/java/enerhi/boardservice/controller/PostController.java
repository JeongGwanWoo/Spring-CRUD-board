package enerhi.boardservice.controller;

import enerhi.boardservice.domain.PostForm;
import enerhi.boardservice.domain.PostStatus;
import enerhi.boardservice.domain.Posts;
import enerhi.boardservice.repository.PostsRepository;
import enerhi.boardservice.service.PostsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDateTime;
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
        post.setPostDate(LocalDateTime.now());
        post.setViews(0);
        post.setStatus(PostStatus.INCLUDE);

        postsService.save(post);

        return "redirect:/board/list";
    }

    //글 목록 조회
    @GetMapping("/board/list")
    public String postList(Model model) {
        List<Posts> posts = postsService.findPosts();
        model.addAttribute("posts", posts);
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
}
