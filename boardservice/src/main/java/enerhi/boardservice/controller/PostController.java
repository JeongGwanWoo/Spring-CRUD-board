package enerhi.boardservice.controller;

import enerhi.boardservice.domain.*;
import enerhi.boardservice.service.PostsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

        postsService.save(post);

        return "redirect:/board/list";
    }

    SearchStatus searchStatus = SearchStatus.TITLE;

    //글 목록 조회
    @GetMapping("/board/list")
    public String postList(Model model,
                           @RequestParam(value = "postnumber", required = false, defaultValue = "30") int pagePostNumber,
                           @RequestParam(value = "page", required = false, defaultValue = "1") int nowPage,
                           @RequestParam(value = "type", required = false, defaultValue = "title") String type,
                           @RequestParam(value = "keyward", required = false) String keyward) {

        log.info("검색 유형 : " + type);
        log.info("검색 키워드 : " + keyward);

        SearchList(model, pagePostNumber, nowPage, type, keyward);

        return "board/list";
    }

    //글 검색
    private void SearchList(Model model, int pagePostNumber, int nowPage, String type, String keyward) {
        if (keyward != null) { //검색 키워드가 있을 경우
            if (type.equals("title")) { //검색 타입이 제목일 경우
                searchStatus = SearchStatus.TITLE; //검색 상태를 타이틀로 변경

                List<Posts> postAll = postsService.postSearch(type, keyward); //타입과 키워드로 글을 가져옴

                //페이지에 보여줄 글 수: 기본값 30, 모든 글 개수로 페이지 수 세기
                int pageNumber = pageNumber(pagePostNumber, postAll);
                int startPage = Math.max(nowPage - 4, 1); //시작 페이지
                int endPage = Math.min(nowPage + 4, pageNumber); //현재 페이지에 대한 보여줄 끝 페이지

                //타입, 키워드, 현재페이지, 페이지에 보여줄 글 수로 해당하는 글을 가져옴
                List<Posts> posts = postsService.postSearch(type, keyward, nowPage, pagePostNumber);

                //현재 페이지가 5이하일 경우 + 총 페이지가 9 이상일 경우
                if (nowPage <= 5 && pageNumber >= 9) {
                    model.addAttribute("endPage", 9); //5페이지 까지는 끝 페이지 9로 고정
                } else {
                    model.addAttribute("endPage", endPage); //5페이지 이하 외의 경우 고정x
                }

                model.addAttribute("startPage", startPage);
                model.addAttribute("nowPage", nowPage);
                model.addAttribute("posts", posts);
                model.addAttribute("keyward", keyward);
                model.addAttribute("searchStatus", searchStatus);
            } else { //검색 타입이 이름일 경우
                searchStatus = SearchStatus.NAME; //검색 상태를 이름으로 변경

                List<Posts> postAll = postsService.postSearch(type, keyward);

                int pageNumber = pageNumber(pagePostNumber, postAll);
                int startPage = Math.max(nowPage - 4, 1);
                int endPage = Math.min(nowPage + 4, pageNumber);

                List<Posts> posts = postsService.postSearch(type, keyward, nowPage, pagePostNumber);

                if (nowPage <= 5 && pageNumber >= 9) {
                    model.addAttribute("endPage", 9);
                } else {
                    model.addAttribute("endPage", endPage);
                }

                model.addAttribute("startPage", startPage);
                model.addAttribute("nowPage", nowPage);
                model.addAttribute("posts", posts);
                model.addAttribute("keyward", keyward);
                model.addAttribute("searchStatus", searchStatus);
            }
        } else { //검색 키워드가 없을 경우
            List<Posts> postAll = postsService.findPosts(); //모든 글을 가져옴
            int pageNumber = pageNumber(pagePostNumber, postAll);
            int startPage = Math.max(nowPage - 4, 1);
            int endPage = Math.min(nowPage + 4, pageNumber);

            //현재 페이지와 페이지 당 글 수로 해당하는 글을 가져옴
            List<Posts> posts = postsService.postPage(nowPage, pagePostNumber);

            if (nowPage <= 5 && pageNumber >= 9) {
                model.addAttribute("endPage", 9);
            } else {
                model.addAttribute("endPage", endPage);
            }

            model.addAttribute("startPage", startPage);
            model.addAttribute("nowPage", nowPage);
            model.addAttribute("posts", posts);
            model.addAttribute("keyward", null); // html 검색 칸 초기화
            model.addAttribute("searchStatus", searchStatus);
        }
    }

    //페이지 개수
    public int pageNumber(int pagePostNumber, List<Posts> posts) {
        int postsSize = posts.size(); //글 개수
        double pageNumber = (double) postsSize / pagePostNumber; //글 개수 / 페이지 당 글 수: 나머지 필요 double
        if (pageNumber % 1 < 1) { //나머지가 있을 경우
            return (int) pageNumber + 1; //글이 남아있으므로 +1
        } else {
            return (int) pageNumber;
        }
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
                           @RequestParam(value = "postnumber", required = false, defaultValue = "30") int pagePostNumber,
                           @RequestParam(value = "page", required = false, defaultValue = "1") int nowPage,
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

        model.addAttribute("post", postForm);

        SearchList(model, pagePostNumber, nowPage, type, keyward);

        model.addAttribute("form", postForm);
        return "/board/show";
    }

    @GetMapping("board/login")
    public String login() {

        return "/board/auth/login";
    }

    @GetMapping("board/signup")
    public String join() {
        return "/board/auth/signup";
    }

}
