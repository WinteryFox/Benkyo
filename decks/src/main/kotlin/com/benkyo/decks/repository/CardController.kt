package com.benkyo.decks.repository

import com.benkyo.decks.data.Card
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import reactor.core.publisher.Flux

interface CardController : CoroutineCrudRepository<Card, String> {
    @Query(
        """
SELECT c.id, c.deck, c.question, array_agg(a.src)
FROM cards c
         LEFT OUTER JOIN card_progress cp on c.id = cp.card AND cp.user = :user
         LEFT JOIN answers a on c.id = a.card
WHERE c.deck = :deck
  AND cp.card IS NULL
GROUP BY c.id
            """
    )
    fun findNewCardsByDeckAndUser(deck: String, user: String): Flux<Card>

    @Query(
        """
SELECT c.*, array_agg(a.src)
FROM cards c
         LEFT JOIN answers a on c.id = a.card
WHERE deck = :deck
GROUP BY c.id
        """
    )
    fun findAllByDeck(deck: String): Flux<Card>
}
