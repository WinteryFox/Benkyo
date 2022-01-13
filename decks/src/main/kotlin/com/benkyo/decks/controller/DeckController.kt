package com.benkyo.decks.controller

import com.benkyo.decks.data.Deck
import com.benkyo.decks.repository.AnswerRepository
import com.benkyo.decks.repository.CardRepository
import com.benkyo.decks.repository.DeckRepository
import com.benkyo.decks.repository.UserRepository
import com.benkyo.decks.request.DeckCreateRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ServerWebExchange
import java.security.Principal
import java.time.LocalDateTime
import java.util.*
import javax.validation.Valid

@RestController
@RequestMapping("/decks")
class DeckController(
    private val userRepository: UserRepository,
    private val deckRepository: DeckRepository,
    private val cardRepository: CardRepository,
    private val answerRepository: AnswerRepository
) {
    @GetMapping
    suspend fun getDecks(): Flow<Deck> = deckRepository.findAll().filter { !it.isPrivate }

    @PostMapping(consumes = [APPLICATION_JSON_VALUE], produces = [APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.CREATED)
    suspend fun createDeck(
        exchange: ServerWebExchange,
        principal: Principal,
        @Valid @RequestBody request: DeckCreateRequest
    ): Deck? {
        if (userRepository.findById(principal.name) == null) {
            exchange.response.statusCode = HttpStatus.UNAUTHORIZED
            return null
        }

        return deckRepository.save(
            Deck(
                UUID.randomUUID().toString(),
                principal.name,
                request.isPrivate,
                request.name,
                request.shortDescription,
                request.description,
                request.sourceLanguage,
                request.targetLanguage,
                LocalDateTime.now(),
                null,
                0
            )
        )
    }

    @PatchMapping("/{id}", consumes = [APPLICATION_JSON_VALUE], produces = [APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.OK)
    suspend fun updateDeck(
        exchange: ServerWebExchange,
        principal: Principal,
        @PathVariable id: String,
        @Valid @RequestBody request: DeckCreateRequest
    ): Deck? {
        val user = userRepository.findById(principal.name)
        if (user == null) {
            exchange.response.statusCode = HttpStatus.UNAUTHORIZED
            return null
        }

        val deck = deckRepository.findById(id)
        if (deck == null) {
            exchange.response.statusCode = HttpStatus.BAD_REQUEST
            return null
        }

        if (deck.author != user.id) {
            exchange.response.statusCode = HttpStatus.FORBIDDEN
            return null
        }

        return deckRepository.save(
            Deck(
                deck.id,
                deck.author,
                request.isPrivate,
                request.name,
                request.shortDescription,
                request.description,
                request.sourceLanguage,
                request.targetLanguage,
                deck.createdAt,
                deck.imageHash, // TODO
                deck.version
            )
        )
    }

    @GetMapping("/{id}", produces = [APPLICATION_JSON_VALUE])
    suspend fun getDeck(@PathVariable id: String): Deck? = deckRepository.findById(id)

    @DeleteMapping("/{id}")
    suspend fun deleteDeck(
        principal: Principal,
        exchange: ServerWebExchange,
        @PathVariable id: String
    ) {
        val user = userRepository.findById(principal.name)
        if (user == null) {
            exchange.response.statusCode = HttpStatus.UNAUTHORIZED
            return
        }

        val deck = deckRepository.findById(id)
        if (deck == null) {
            exchange.response.statusCode = HttpStatus.BAD_REQUEST
            return
        }

        if (deck.author != user.id) {
            exchange.response.statusCode = HttpStatus.FORBIDDEN
            return
        }

        // TODO: Delete answers and cards
        deckRepository.deleteById(id)
    }
}
