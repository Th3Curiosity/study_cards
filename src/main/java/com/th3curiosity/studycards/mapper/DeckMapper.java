package com.th3curiosity.studycards.mapper;

import com.th3curiosity.studycards.dto.deck.DeckResponse;
import com.th3curiosity.studycards.entity.Deck;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DeckMapper {
    public DeckResponse toDeckResponseDTO(Deck deck) {
        DeckResponse deckResponse = new DeckResponse();
        deckResponse.setId(deck.getId());
        deckResponse.setTitle(deck.getTitle());
        deckResponse.setDescription(deck.getDescription());
        deckResponse.setCreatedAt(deck.getCreatedAt());
        deckResponse.setUpdatedAt(deck.getUpdatedAt());
        return deckResponse;
    }

    public List<DeckResponse> toDeckResponseList(List<Deck> decks) {
        return decks.stream()
                .map(this::toDeckResponseDTO)
                .toList();
    }
}
