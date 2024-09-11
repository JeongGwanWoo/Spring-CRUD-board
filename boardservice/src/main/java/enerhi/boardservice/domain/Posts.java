package enerhi.boardservice.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
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
    private String content; //글 내용

    private LocalDateTime postDate; //생성 되었을 때
    private LocalDateTime postEditDate; //수정 되었을 때
    private LocalDateTime postDeleteDate; //삭제 되었을 때
    private int views; //조회수
    private PostStatus status; // [INCLUDE, DELETE]
}
