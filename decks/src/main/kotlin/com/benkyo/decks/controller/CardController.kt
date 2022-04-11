package com.benkyo.decks.controller

import com.benkyo.decks.data.*
import com.benkyo.decks.exceptions.CardNotFoundException
import com.benkyo.decks.exceptions.ColumnsNotFound
import com.benkyo.decks.exceptions.DeckNotFoundException
import com.benkyo.decks.repository.*
import com.benkyo.decks.request.CardCreateRequest
import com.benkyo.decks.request.CardUpdateRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactor.awaitSingle
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
            // TODO: Consider HTTP status code
            // For consistency, this always returns a Not Found if the deck exists, but the user can't access it.
            // We should consider whether we prefer this, or we'd rather respond with Forbidden and leak that the
            // deck exists.

            throw DeckNotFoundException(id)
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

        if (deck == null || (deck.isPrivate && deck.author == principal.name)) {
            throw DeckNotFoundException(id)
        }

        var card = cardRepository.get(id, cardId).awaitSingle()

        if (card == null) {
            throw CardNotFoundException(cardId)
        }

        // Ensure that all the columns exist first
        val columnIds = columnRepository.findAllByDeck(id).toList().map { it.id }

        if (request.data != null) {
            val unknownColumns = request.data
                .filter { it.column !in columnIds }
                .map { it.column }

            if (unknownColumns.isNotEmpty()) {
                throw ColumnsNotFound(unknownColumns.toTypedArray())
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

        if (deck == null || (deck.isPrivate && deck.author == principal.name)) {
            throw DeckNotFoundException(id)
        }

        // Ensure that all the columns exist first
        val columnIds = columnRepository.findAllByDeck(id).toList().map { it.id }

        val unknownColumns = request.data
            .filter { it.column !in columnIds }
            .map { it.column }

        if (unknownColumns.isNotEmpty()) {
            throw ColumnsNotFound(unknownColumns.toTypedArray())
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

        if (deck == null || (deck.isPrivate && deck.author == principal.name)) {
            throw DeckNotFoundException(id)
        }

        answerRepository.deleteAllByCard(card)
        cardRepository.deleteById(card).awaitSingle()
    }
}
