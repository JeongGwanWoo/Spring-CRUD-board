package enerhi.boardservice.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class Posts {

    private Long id;

    private String name;
    private String title;
    private String content;
    private LocalDateTime postdate;
    private int views;
    private PostStatus status; // [INCLUDE, DELETE]
}
