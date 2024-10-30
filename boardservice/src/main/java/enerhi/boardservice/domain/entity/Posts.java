package enerhi.boardservice.domain.entity;

import enerhi.boardservice.domain.PostStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class Posts {

    @Id @GeneratedValue
    @Column(name = "post_id")
    private Long id;

    private String name; //글 작성자 이름
    private String title; //글 제목
    @Column(length = 500)
    private String content; //글 내용

    private String postDate; //생성 되었을 때
    private LocalDateTime postDate2; //생성 되었을 때
    private String postEditDate; //수정 되었을 때
    private String postDeleteDate; //삭제 되었을 때
    private int views; //조회수

    @Enumerated(EnumType.STRING)
    private PostStatus status; // [INCLUDE, DELETE]
}
