package com.th3curiosity.studycards.dto.other;

public class JwtResponse {
    private final String accessToken;

    public JwtResponse(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
