package com.benkyo.decks.repository

import com.benkyo.decks.data.CardWithData
import net.lecousin.reactive.data.relational.query.SelectQuery
import net.lecousin.reactive.data.relational.query.criteria.Criteria
import net.lecousin.reactive.data.relational.repository.LcR2dbcRepository
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import reactor.core.publisher.Flux

/*interface CardWithDataRepository : LcR2dbcRepository<CardWithData, String> {
*//*    @Query(
        """
SELECT c.id, c.deck, c.ordinal, array_agg(d) as data
FROM cards c
         LEFT JOIN card_data d on c.id = d.card
WHERE c.deck = :deck
GROUP BY c.id
        """
    )
    fun findAllByDeck(deck: String): Flux<CardWithData>*//*
    fun findAllByDeck(deck: String): Flux<CardWithData> =
        SelectQuery.from(CardWithData::class.java, "c")
            .join("c", "card", "card")
            .where(Criteria.property("c", "deck").`is`(deck))
            .execute(lcClient)
}*/
