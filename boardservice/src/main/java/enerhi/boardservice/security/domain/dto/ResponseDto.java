package enerhi.boardservice.security.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ResponseDto {

    private boolean success;
    private String text;

    public ResponseDto(boolean success, String text) {
        this.success = success;
        this.text = text;
    }
}
