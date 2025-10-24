package com.th3curiosity.studycards.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Запрос входа")
public class LoginRequest {
    @Schema(description = "username")
    private String username;
    @Schema(description = "password")
    private String password;


    public LoginRequest() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
