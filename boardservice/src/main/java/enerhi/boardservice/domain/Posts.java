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

    private String name;
    private String title;
    private String content;

    private LocalDateTime postDate;
    private LocalDateTime postEditDate;
    private int views;
    private PostStatus status; // [INCLUDE, DELETE]
}
