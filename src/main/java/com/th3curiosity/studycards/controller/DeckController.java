package com.th3curiosity.studycards.controller;

import com.th3curiosity.studycards.dto.card.CardCreateRequest;
import com.th3curiosity.studycards.dto.card.CardResponse;
import com.th3curiosity.studycards.dto.deck.DeckCreateRequest;
import com.th3curiosity.studycards.dto.deck.DeckResponse;
import com.th3curiosity.studycards.dto.other.ErrorResponse;
import com.th3curiosity.studycards.service.DeckService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/decks")
public class DeckController {

    private final DeckService deckService;

    public DeckController(DeckService deckService) {
        this.deckService = deckService;
    }

    @Operation(
            summary = "Получить массив колод пользователя",
            description = "Возвращает массив из колод без карт в них. Массив может быть пустой"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Колоды успешно получены",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = DeckResponse.class)
            )
    )
    @GetMapping()
    public ResponseEntity<List<DeckResponse>> getAllMyDecks(@AuthenticationPrincipal UserDetails userDetails) {
        List<DeckResponse> userDecks = deckService.getUserDecks(userDetails.getUsername());
        return ResponseEntity.ok(userDecks);
    }


    @Operation(
            summary = "Создать новую колоду",
            description = "Создаёт колоду и возвращает созданную колоду"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Колода успешно создана",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = DeckResponse.class)
            )
    )
    @PostMapping()
    public ResponseEntity<?> createNewDeck(@RequestBody DeckCreateRequest request,
                                           @AuthenticationPrincipal UserDetails userDetails) {
        DeckResponse createdDeck = deckService.createDeck(request, userDetails.getUsername());
        return ResponseEntity.ok(createdDeck);
    }


    @Operation(
            summary = "Добавить карту в колоду",
            description = "Добавляет карту в колоду (id колоды берётся из пути) и возвращает добавленную карту"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Карта успешно добавлена",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = CardResponse.class)
            )
    )
    @ApiResponse(
            responseCode = "404",
            description = "Колода не найдена или принадлежит другому пользователю",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))
    )
    @PostMapping("/{id}/add-card")
    public ResponseEntity<?> addCard(@PathVariable Long id,
                                     @AuthenticationPrincipal UserDetails userDetails,
                                     @RequestBody CardCreateRequest cardCreateRequest) {
        CardResponse response = deckService.addCardToDeck(userDetails.getUsername(), id, cardCreateRequest);
        return ResponseEntity.ok(response);
    }
}
