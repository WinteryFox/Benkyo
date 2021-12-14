package com.benkyo.decks.repository

import com.benkyo.decks.data.Card
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import reactor.core.publisher.Flux

interface CardRepository : CoroutineCrudRepository<Card, String> {
    @Query(
        """
SELECT *
FROM cards_with_answers_view
WHERE deck = :deck
        """
    )
    fun findAllByDeck(deck: String): Flux<Card>
}
