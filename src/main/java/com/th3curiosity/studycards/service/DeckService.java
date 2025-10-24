package com.th3curiosity.studycards.service;

import com.th3curiosity.studycards.dto.card.CardCreateRequest;
import com.th3curiosity.studycards.dto.card.CardResponse;
import com.th3curiosity.studycards.dto.deck.DeckCreateRequest;
import com.th3curiosity.studycards.dto.deck.DeckResponse;
import com.th3curiosity.studycards.entity.Card;
import com.th3curiosity.studycards.entity.Deck;
import com.th3curiosity.studycards.entity.User;
import com.th3curiosity.studycards.exceptions.DeckNotFoundException;
import com.th3curiosity.studycards.mapper.CardMapper;
import com.th3curiosity.studycards.mapper.DeckMapper;
import com.th3curiosity.studycards.repository.CardRepository;
import com.th3curiosity.studycards.repository.DeckRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeckService {

    private final DeckRepository deckRepository;
    private final UserService userService;
    private final DeckMapper deckMapper;
    private final CardRepository cardRepository;
    private final CardMapper cardMapper;

    public DeckService(DeckRepository deckRepository, UserService userService, DeckMapper deckMapper, CardRepository cardRepository, CardMapper cardMapper) {
        this.deckRepository = deckRepository;
        this.userService = userService;
        this.deckMapper = deckMapper;
        this.cardRepository = cardRepository;
        this.cardMapper = cardMapper;
    }

    public List<DeckResponse> getUserDecks(String username) {
        User user = userService.findByUsername(username);
        List<Deck> decks = deckRepository.findByUser(user);
        return deckMapper.toDeckResponseList(decks);
    }

    public DeckResponse createDeck(DeckCreateRequest request, String username) {
        User user = userService.findByUsername(username);
        Deck deck = new Deck(user, request.getTitle(), request.getDescription());
        deckRepository.save(deck);
        return deckMapper.toDeckResponseDTO(deck);
    }

    public CardResponse addCardToDeck(String username, Long deckId, CardCreateRequest cardCreateRequest) {
        User user = userService.findByUsername(username);
        Deck deck = deckRepository.findByUserAndId(user, deckId)
                .orElseThrow(() -> new DeckNotFoundException(deckId, username)); //if user is not owner of the deck or deck does not exist
        Card card = cardMapper.toCard(deck, cardCreateRequest);
        cardRepository.save(card);
        return cardMapper.toCardResponseDTO(card);
    }
}
