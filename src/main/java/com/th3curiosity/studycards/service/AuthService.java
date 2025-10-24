package com.th3curiosity.studycards.service;

import com.th3curiosity.studycards.dto.other.AuthResult;
import com.th3curiosity.studycards.dto.user.LoginRequest;
import com.th3curiosity.studycards.dto.user.SignupRequest;
import com.th3curiosity.studycards.entity.User;
import com.th3curiosity.studycards.exceptions.InvalidUsernameOrPasswordException;
import com.th3curiosity.studycards.repository.RefreshTokenRepository;
import com.th3curiosity.studycards.security.JwtUtils;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserService userService;
    private final RefreshTokensService refreshTokensService;
    private final PasswordEncoder passwordEncoder;

    public AuthService(AuthenticationManager authenticationManager,
                       JwtUtils jwtUtils,
                       UserService userService, RefreshTokenRepository refreshTokenRepository, RefreshTokensService refreshTokensService,
                       PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.userService = userService;
        this.refreshTokensService = refreshTokensService;
        this.passwordEncoder = passwordEncoder;
    }


    public AuthResult login(LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );

            String username = request.getUsername();
            User user = userService.findByUsername(username);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwtAccess = jwtUtils.generateAccessToken(username);
            String jwtRefresh = jwtUtils.generateRefreshToken(username);
            refreshTokensService.saveRefreshToken(user, jwtRefresh);

            ResponseCookie refreshCookie = jwtUtils.putRefreshTokenInCookie(jwtRefresh);

            return new AuthResult(true, jwtAccess, refreshCookie, "Login successful");
        } catch (BadCredentialsException exception) {
            throw new InvalidUsernameOrPasswordException();
        }
    }


    public AuthResult signup(SignupRequest request) {
        if (userService.existsByUsername(request.getUsername())) {
            return new AuthResult(false, null, null, "Username is already taken");
        }

        User user = new User(
                request.getUsername(),
                passwordEncoder.encode(request.getPassword())
        );
        userService.save(user);
        return new AuthResult(true, null, null, "User registered successfully");
    }


    public AuthResult refresh(String refreshToken) {
        if (jwtUtils.validateToken(refreshToken)) {
            String username = jwtUtils.getUsernameFromToken(refreshToken);
            User user = userService.findByUsername(username);

            if (user != null && refreshTokensService.isRefreshTokenInWhiteList(user, refreshToken)) { //checks if refreshToken is in WhiteList
                String newJWTAccess = jwtUtils.generateAccessToken(username);
                String newJWTRefresh = jwtUtils.generateRefreshToken(username);

                refreshTokensService.deleteRefreshToken(user, refreshToken);
                refreshTokensService.saveRefreshToken(user, newJWTRefresh);

                ResponseCookie newRefreshCookie = jwtUtils.putRefreshTokenInCookie(newJWTRefresh);

                return new AuthResult(true, newJWTAccess, newRefreshCookie, "Tokens refreshed successfully");
            }
        }

        return new AuthResult(false, null, null, "Invalid refresh token");
    }

    public AuthResult logout(String refreshToken, String logoutType) {
        if (jwtUtils.validateToken(refreshToken)) {
            String username = jwtUtils.getUsernameFromToken(refreshToken);
            User user = userService.findByUsername(username);

            if (refreshTokensService.isRefreshTokenInWhiteList(user, refreshToken)) {
                switch (logoutType) {
                    case "all":
                        refreshTokensService.deleteAllRefreshTokens(user);
                        break;
                    case "current":
                        refreshTokensService.deleteRefreshToken(user, refreshToken);
                        break;
                }
                return new AuthResult(true, null, null, "Logout complete successfully");
            }
        }
        return new AuthResult(false, null, null, "Invalid refresh token");
    }
}
