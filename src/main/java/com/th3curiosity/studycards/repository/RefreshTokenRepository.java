package com.th3curiosity.studycards.repository;

import com.th3curiosity.studycards.entity.RefreshToken;
import com.th3curiosity.studycards.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    List<RefreshToken> findByUser(User user);

    boolean existsByRefreshToken(String refreshToken);

    void deleteAllByUser(User user);

    void deleteByUserAndRefreshToken(User user, String refreshToken);
}
