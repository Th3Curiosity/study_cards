package com.th3curiosity.studycards.dto.other;

import org.springframework.http.ResponseCookie;

public class AuthResult {

    private final boolean success;
    private final String accessToken;
    private final ResponseCookie refreshTokenCookie;
    private final String message;

    public AuthResult(boolean success, String accessToken, ResponseCookie refreshTokenCookie, String message) {
        this.success = success;
        this.accessToken = accessToken;
        this.refreshTokenCookie = refreshTokenCookie;
        this.message = message;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public ResponseCookie getRefreshTokenCookie() {
        return refreshTokenCookie;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
