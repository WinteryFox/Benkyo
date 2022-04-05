package com.benkyo.decks.controller

import com.benkyo.decks.data.CardWithDataOrdinal
import com.benkyo.decks.data.CardDataWithOrdinal
import com.benkyo.decks.data.CardsWithData
import com.benkyo.decks.repository.ColumnRepository
import com.benkyo.decks.repository.*
import kotlinx.coroutines.*
import kotlinx.coroutines.GlobalScope.coroutineContext
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ServerWebExchange
import java.security.Principal

@RestController
@RequestMapping("/decks/{id}/cards")
class CardController(
    private val deckRepository: DeckRepository,
    private val cardRepository: CardRepository,
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
        }

        val awaitedColumns = columns.await()
        return@coroutineScope CardsWithData(
            awaitedColumns,
            cards.await().map { card ->
                CardWithDataOrdinal(
                    card.id,
                    card.ordinal,
                    card.data.map { data ->
                        CardDataWithOrdinal(
                            data.column,
                            awaitedColumns.find { data.column == it.id }!!.ordinal,
                            data.src
                        )
                    }
                )
            }.toList()
        )
    }

    /*@PostMapping
    suspend fun createCard(
        principal: Principal,
        exchange: ServerWebExchange,
        @PathVariable id: String,
        @RequestBody request: CreateCardRequest
    ): CardWithAnswers? {
        val deck = deckRepository.findById(id)
        if (deck == null) {
            exchange.response.statusCode = HttpStatus.BAD_REQUEST
            return null
        }
        if (deck.author != principal.name) {
            exchange.response.statusCode = HttpStatus.FORBIDDEN
            return null
        }

        val card = cardRepository.save(Card(UUID.randomUUID().toString(), id, 0))
        request.answers.forEach {
            answerRepository.save(Answer(card.id, it))
        } // TODO
        //answerRepository.saveAll(request.answers.map { Answer(UUID.randomUUID().toString(), card.id, it, 0) })
        return CardWithAnswers(
            card.id,
            deck.id,
            card.question,
            request.answers
        )
    }*/

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
