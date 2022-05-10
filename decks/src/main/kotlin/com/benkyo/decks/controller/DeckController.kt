package com.benkyo.decks.controller

import com.benkyo.decks.data.Deck
import com.benkyo.decks.exceptions.DeckNotFoundException
import com.benkyo.decks.exceptions.InvalidLocaleException
import com.benkyo.decks.exceptions.UnauthorizedException
import com.benkyo.decks.repository.DeckRepository
import com.benkyo.decks.repository.UserRepository
import com.benkyo.decks.request.DeckCreateRequest
import com.benkyo.decks.request.DeckPatchRequest
import com.benkyo.decks.utils.isValidLocaleCode
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
        userRepository.findById(principal.name)
            ?: throw UnauthorizedException()

        if (!request.sourceLanguage.isValidLocaleCode())
            throw InvalidLocaleException(request.sourceLanguage)
        if (!request.targetLanguage.isValidLocaleCode())
            throw InvalidLocaleException(request.targetLanguage)

        return deckRepository.save(
            Deck(
                UUID.randomUUID().toString(),
                principal.name,
                request.isPrivate,
                LocalDateTime.now(),
                request.name,
                request.shortDescription,
                request.description,
                request.sourceLanguage,
                request.targetLanguage,
                null
            )
        )
    }

    @PatchMapping("/{id}", consumes = [APPLICATION_JSON_VALUE], produces = [APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.OK)
    suspend fun updateDeck(
        exchange: ServerWebExchange,
        principal: Principal,
        @PathVariable id: String,
        @Valid @RequestBody request: DeckPatchRequest
    ): Deck? {
        val user = userRepository.findById(principal.name)
            ?: throw UnauthorizedException()

        val deck = deckRepository.findById(id)

        // TODO: See TODO in CardController about status codes
        if (deck == null || deck.author != user.id)
            throw DeckNotFoundException(id)

        if (request.sourceLanguage != null && !request.sourceLanguage.isValidLocaleCode())
            throw InvalidLocaleException(request.sourceLanguage)
        if (request.targetLanguage != null && !request.targetLanguage.isValidLocaleCode())
            throw InvalidLocaleException(request.targetLanguage)

        return deckRepository.save(
            deck.copy(
                description = request.description ?: deck.description,
                isPrivate = request.isPrivate ?: deck.isPrivate,
                name = request.name ?: deck.name,
                shortDescription = request.shortDescription ?: deck.shortDescription,
                sourceLanguage = request.sourceLanguage ?: deck.sourceLanguage,
                targetLanguage = request.targetLanguage ?: deck.targetLanguage
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

        if (deck == null || deck.author != user.id) {
            // TODO: See TODO in CardController about status codes

            throw DeckNotFoundException(id)
        }

        deckRepository.deleteById(id)
    }
}
