package com.benkyo.decks.repository

import com.benkyo.decks.data.Card
import com.benkyo.decks.data.CardWithData
import net.lecousin.reactive.data.relational.query.SelectQuery
import net.lecousin.reactive.data.relational.query.criteria.Criteria
import net.lecousin.reactive.data.relational.repository.LcR2dbcRepository
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import reactor.core.publisher.Flux

interface CardRepository : LcR2dbcRepository<Card, String> {
//@Query(
//    """
//SELECT c.id, c.deck, c.ordinal, d.*
//FROM cards c
//        LEFT JOIN card_data d on c.id = d.card
//WHERE c.deck = :deck
//    """
//)
    fun findAllByDeck(deck: String): Flux<Card> =
        SelectQuery.from(Card::class.java, "card")
            .where(Criteria.property("card", "deck").`is`(deck))
            .join("card", "data", "card_data")
            .execute(lcClient)
}
