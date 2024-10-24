package enerhi.boardservice.security.auth.jwt;

public interface JwtProperties {
    String SECRET = "ener";
    int EXPIRATION_TIME = 60000 * 10;
    String TOKEN_PREFIX = "Bearer ";
    String HEADER_STRING = "Authorization";
}
