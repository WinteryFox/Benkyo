package com.benkyo.decks.controller

import com.benkyo.decks.data.*
import com.benkyo.decks.repository.*
import com.benkyo.decks.request.CardCreateRequest
import com.benkyo.decks.request.CardUpdateRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
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
    suspend fun getCards(
        principal: Principal,
        exchange: ServerWebExchange,
        @PathVariable id: String
    ): CardsWithData? = coroutineScope {
        val deck = deckRepository.findById(id)
        if (deck == null || (deck.isPrivate && deck.author == principal.name)) {
            exchange.response.statusCode = HttpStatus.NOT_FOUND
            return@coroutineScope null
        }

        val columns = async(Dispatchers.IO) {
            columnRepository.findAllByDeck(id).toList()
        }

        val cards = async(Dispatchers.IO) {
            cardRepository.findAllByDeck(id).toList()
        }.await()

        val awaitedColumns = columns.await()
        return@coroutineScope CardsWithData(
            awaitedColumns,
            cards.map { card ->
                CardWithDataOrdinal(
                    card.id,
                    card.ordinal,
                    card.data.map { data ->
                        CardDataWithOrdinal(
                            data.column,
                            awaitedColumns.find { data.column == it.id }!!.ordinal,
                            data.src
                        )
                    },
                    card.attachments
                )
            }.toList()
        )
    }

    @PatchMapping("/{cardId}")
    suspend fun updateCard(
        principal: Principal,
        exchange: ServerWebExchange,
        @PathVariable id: String,
        @PathVariable cardId: String,
        @RequestBody request: CardUpdateRequest
    ): Card? {
        val deck = deckRepository.findById(id)

        if (deck == null) {
            exchange.response.statusCode = HttpStatus.NOT_FOUND
            return null
        }

        if (deck.author != principal.name) {
            exchange.response.statusCode = HttpStatus.FORBIDDEN
            return null
        }

        var card = cardRepository.get(id, cardId).awaitSingle()

        if (card == null) {
            exchange.response.statusCode = HttpStatus.NOT_FOUND
            return null
        }

        // Ensure that all the columns exist first
        val columnIds = columnRepository.findAllByDeck(id).toList().map { it.id }

        if (request.data != null) {
            if (request.data.any { it.column !in columnIds }) {
                exchange.response.statusCode = HttpStatus.BAD_REQUEST
                return null
            }

            card = card.copy(data = request.data.map {
                CardData(
                    card = card!!.id,
                    column = it.column,
                    src = it.src
                )
            })
        }

        if (request.ordinal != null) {
            card = card.copy(ordinal = request.ordinal)
        }

        cardRepository.save(card).awaitSingle()
        cardDataRepository.saveAll(card.data).toList()

        return card
    }

    @PostMapping
    suspend fun createCard(
        principal: Principal,
        exchange: ServerWebExchange,
        @PathVariable id: String,
        @RequestBody request: CardCreateRequest
    ): Card? {
        val deck = deckRepository.findById(id)

        if (deck == null) {
            exchange.response.statusCode = HttpStatus.NOT_FOUND
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
            exchange.response.statusCode = HttpStatus.NOT_FOUND
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
