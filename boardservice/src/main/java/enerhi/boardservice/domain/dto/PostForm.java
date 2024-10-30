package enerhi.boardservice.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PostForm {

    private Long id;

    private String name;
    private String title;
    private String content;
    private String postDate;
    private int views;
}
