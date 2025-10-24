package com.th3curiosity.studycards.mapper;

import com.th3curiosity.studycards.dto.card.CardCreateRequest;
import com.th3curiosity.studycards.dto.card.CardResponse;
import com.th3curiosity.studycards.entity.Card;
import com.th3curiosity.studycards.entity.Deck;
import org.springframework.stereotype.Component;

@Component
public class CardMapper {
    public CardResponse toCardResponseDTO(Card card) {
        CardResponse cardResponse = new CardResponse();
        cardResponse.setId(card.getId());
        cardResponse.setBack(card.getBack());
        cardResponse.setFront(card.getFront());
        return cardResponse;
    }

    public Card toCard(Deck deck, CardCreateRequest request) {
        Card card = new Card();
        card.setDeck(deck);
        card.setBack(request.getBack());
        card.setFront(request.getFront());
        return card;
    }
}
