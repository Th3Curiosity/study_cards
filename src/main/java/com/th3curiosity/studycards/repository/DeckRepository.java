package com.th3curiosity.studycards.repository;

import com.th3curiosity.studycards.entity.Deck;
import com.th3curiosity.studycards.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DeckRepository extends JpaRepository<Deck, Long> {
    List<Deck> findByUserId(Long userId);
    List<Deck> findByUser(User user);
    Optional<Deck> findByUserAndId(User user, Long id);
}
