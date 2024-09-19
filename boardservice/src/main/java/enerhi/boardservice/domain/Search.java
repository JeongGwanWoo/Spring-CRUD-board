package enerhi.boardservice.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Search {
    private String keyward;
    private String title;
}
