package com.th3curiosity.studycards.controller;

import com.th3curiosity.studycards.dto.other.AuthResult;
import com.th3curiosity.studycards.dto.other.ErrorResponse;
import com.th3curiosity.studycards.dto.other.JwtResponse;
import com.th3curiosity.studycards.dto.user.LoginRequest;
import com.th3curiosity.studycards.dto.user.SignupRequest;
import com.th3curiosity.studycards.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(
            summary = "Аутентификация пользователя",
            description = "Возвращает access token в теле ответа и refresh token в cookie"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Успешная аутентификация",
            headers = {
                    @Header(
                            name = HttpHeaders.SET_COOKIE,
                            description = "Cookie с refresh токеном",
                            schema = @Schema(type = "string", example = "refreshToken=abcd1234; " +
                                    "Path=/api/auth/refresh; HttpOnly; Secure")
                    )
            },
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = JwtResponse.class)
            )
    )
    @ApiResponse(
            responseCode = "401",
            description = "Неверные учетные данные",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)
            ))
    @SecurityRequirements()
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        AuthResult result = authService.login(request);

        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, String.valueOf(result.getRefreshTokenCookie())) //header: cookie with refresh token
                .body(new JwtResponse(result.getAccessToken())); //JWT access token
    }


    @Operation(
            summary = "Регистрация нового пользователя",
            description = "Возвращает только HttpStatus, говоря зарегистрирован пользователь или нет"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Успешная регистрация"
    )
    @ApiResponse(responseCode = "422", description = "Имя пользователя уже занято")
    @SecurityRequirements()
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest request) {
        AuthResult result = authService.signup(request);

        if (result.isSuccess()) {
            return ResponseEntity.ok(result.getMessage());
        }

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(result.getMessage());

    }


    @Operation(
            summary = "Обновление токенов доступа",
            description = "Использует refresh токен из HttpOnly cookie для выдачи нового access токена. \n" +
                    "Возвращает новый access токен в теле ответа и обновлённый refresh токен в Set-Cookie."
    )

    @ApiResponse(
            responseCode = "200",
            description = "Токен успешно обновлён",
            headers = {
                    @Header(
                            name = HttpHeaders.SET_COOKIE,
                            description = "Cookie с новым refresh токеном (HttpOnly)",
                            schema = @Schema(type = "string", example = "refreshToken=xyz987; " +
                                    "Path=/api/auth/refresh; HttpOnly; Secure")
                    )
            },
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = JwtResponse.class)
            )
    )
    @SecurityRequirements()
    @ApiResponse(responseCode = "401", description = "Недействительный или отсутствующий refresh токен")
    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@CookieValue("refreshToken") String refreshToken) {
        AuthResult result = authService.refresh(refreshToken);

        if (result.isSuccess()) {
            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, String.valueOf(result.getRefreshTokenCookie())) //header: cookie with refresh token
                    .body(new JwtResponse(result.getAccessToken())); //new JWT access token
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result.getMessage());
    }


    @Operation(
            summary = "Сделать активный refresh-токен недействительным",
            description = "Убирает переданный в cookie refresh токен из Whitelist токенов. " +
                    "Требуется refresh токен в http-only cookie"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Refresh токен успешно инвалидирован"
    )
    @ApiResponse(
            responseCode = "401",
            description = "Недействительный или отсутствующий refresh токен"
    )
    @SecurityRequirements()
    @PostMapping("/logout/current")
    public ResponseEntity<?> logoutCurrent(@CookieValue("refreshToken") String refreshToken) {
        AuthResult result = authService.logout(refreshToken, "current");

        if (result.isSuccess()) {
            return ResponseEntity.ok().body(result.getMessage());
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result.getMessage());
    }


    @Operation(
            summary = "Сделать все валидные refresh токены пользователя недействительными",
            description = "Убирает все refresh токены из Whitelist токенов текущего пользователя. " +
                    "Требуется refresh токен в http-only cookie"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Все Refresh токены пользователя успешно инвалидированы"
    )
    @ApiResponse(
            responseCode = "401",
            description = "Недействительный или отсутствующий refresh токен"
    )
    @SecurityRequirements()
    @PostMapping("/logout/all")
    public ResponseEntity<?> logoutAll(@CookieValue("refreshToken") String refreshToken) {
        AuthResult result = authService.logout(refreshToken, "all");

        if (result.isSuccess()) {
            return ResponseEntity.ok().body(result.getMessage());
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result.getMessage());
    }
}
