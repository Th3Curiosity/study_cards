package com.th3curiosity.studycards.service;

import com.th3curiosity.studycards.entity.RefreshToken;
import com.th3curiosity.studycards.entity.User;
import com.th3curiosity.studycards.repository.RefreshTokenRepository;
import com.th3curiosity.studycards.security.EncriptionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Transactional
@Service
public class RefreshTokensService {

    @Value("${jwt.refresh.expirationMs}")
    private Long jwtRefreshExpirationMs;

    private final RefreshTokenRepository refreshTokenRepository;
    private final EncriptionUtils encriptionUtils;

    public RefreshTokensService(RefreshTokenRepository refreshTokenRepository, EncriptionUtils encriptionUtils) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.encriptionUtils = encriptionUtils;
    }


    public void saveRefreshToken(User user, String refreshToken) {
        RefreshToken refreshTokenEntity = new RefreshToken();
        refreshTokenEntity.setUser(user);
        refreshTokenEntity.setRefreshToken(encriptionUtils.encode(refreshToken));
        refreshTokenEntity.setExpiresAt(new Date(System.currentTimeMillis() + jwtRefreshExpirationMs));
        refreshTokenRepository.save(refreshTokenEntity);
    }

    public void deleteAllRefreshTokens(User user) {
        refreshTokenRepository.deleteAllByUser(user);
    }

    public void deleteRefreshToken(User user, String refreshToken) {
        List<RefreshToken> tokens = refreshTokenRepository.findByUser(user);

        RefreshToken matchingToken = findMatchingToken(tokens, refreshToken);

        if (matchingToken != null) {
            refreshTokenRepository.delete(matchingToken);
        }
    }

    public boolean isRefreshTokenInWhiteList(User user, String refreshToken) {
        List<RefreshToken> tokens = refreshTokenRepository.findByUser(user);

        RefreshToken matchingToken = findMatchingToken(tokens, refreshToken);

        return matchingToken != null;
    }


    private RefreshToken findMatchingToken(List<RefreshToken> tokens, String refreshToken) {
        RefreshToken matchingToken = null;

        for (RefreshToken token : tokens) {
            if (encriptionUtils.matches(refreshToken, token.getRefreshToken())) {
                matchingToken = token;
                break;
            }
        }

        return matchingToken;
    }

}
