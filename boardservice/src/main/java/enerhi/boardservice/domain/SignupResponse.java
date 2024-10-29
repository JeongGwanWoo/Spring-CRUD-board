package enerhi.boardservice.domain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SignupResponse {

    private boolean success;
    private String message;

    public SignupResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
}
