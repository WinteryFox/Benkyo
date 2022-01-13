package com.benkyo.decks.controller

import com.benkyo.decks.data.Answer
import com.benkyo.decks.data.Card
import com.benkyo.decks.data.CardWithAnswers
import com.benkyo.decks.repository.*
import com.benkyo.decks.request.CreateCardRequest
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ServerWebExchange
import java.security.Principal
import java.util.*

@RestController
@RequestMapping("/decks/{id}/cards")
class CardController(
    private val userRepository: UserRepository,
    private val deckRepository: DeckRepository,
    private val cardRepository: CardRepository,
    private val cardWithAnswersRepository: CardWithAnswersRepository,
    private val answerRepository: AnswerRepository
) {
    @GetMapping
    fun getCards(@PathVariable id: String) = cardWithAnswersRepository.findAllByDeck(id)

    @PostMapping
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

        val card = cardRepository.save(Card(UUID.randomUUID().toString(), id, request.question, 0))
        request.answers.forEach {
            answerRepository.save(Answer(UUID.randomUUID().toString(), card.id, it, 0))
        } // TODO
        //answerRepository.saveAll(request.answers.map { Answer(UUID.randomUUID().toString(), card.id, it, 0) })
        return CardWithAnswers(
            card.id,
            card.deck,
            card.question,
            request.answers
        )
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
        cardRepository.deleteById(card)
    }
}
