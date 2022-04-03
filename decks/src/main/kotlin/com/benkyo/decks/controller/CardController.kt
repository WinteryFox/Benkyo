package com.benkyo.decks.controller

import com.benkyo.decks.data.Card
import com.benkyo.decks.data.CardData
import com.benkyo.decks.repository.*
import com.benkyo.decks.request.CardCreateRequest
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ServerWebExchange
import java.security.Principal
import java.util.*

@RestController
@RequestMapping("/decks/{id}/cards")
class CardController(
    private val deckRepository: DeckRepository,
    private val cardRepository: CardRepository,
    private val cardDataRepository: CardDataRepository,
    private val columnRepository: ColumnRepository,
    private val answerRepository: AnswerRepository
) {
    @GetMapping
    fun getCards(@PathVariable id: String) = cardRepository.findAllByDeck(id)

    @PostMapping
    suspend fun createCard(
        principal: Principal,
        exchange: ServerWebExchange,
        @PathVariable id: String,
        @RequestBody request: CardCreateRequest
    ): Card? {
        val deck = deckRepository.findById(id)

        if (deck == null) {
            exchange.response.statusCode = HttpStatus.BAD_REQUEST
            return null
        }

        if (deck.author != principal.name) {
            exchange.response.statusCode = HttpStatus.FORBIDDEN
            return null
        }

        // Ensure that all the columns exist first
        val columnIds = columnRepository.findAllByDeck(id).toList().map { it.id }

        if (request.data.any { it.column !in columnIds }) {
            exchange.response.statusCode = HttpStatus.BAD_REQUEST
            return null
        }

        val card = cardRepository.save(
            Card(
                id = UUID.randomUUID().toString(),
                deck = id,
                ordinal = request.ordinal
            )
        ).awaitSingle()

        val data = cardDataRepository.saveAll(request.data.map {
            CardData(
                card = card.id,
                column = it.column,
                src = it.src
            )
        }).toList()

        return card.copy(data = data)
    }

    @DeleteMapping("/{card}")
    suspend fun deleteCard(
        principal: Principal,
        exchange: ServerWebExchange,
        @PathVariable id: String,
        @PathVariable card: String
    ) {
        val deck = deckRepository.findById(id)

        if (deck == null) {
            exchange.response.statusCode = HttpStatus.BAD_REQUEST
            return
        }

        if (deck.author != principal.name) {
            exchange.response.statusCode = HttpStatus.FORBIDDEN
            return
        }

        answerRepository.deleteAllByCard(card)
        cardRepository.deleteById(card).awaitSingle()
    }
}
