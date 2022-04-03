package com.benkyo.decks.repository

import com.benkyo.decks.data.Card
import com.benkyo.decks.jooq.Tables
import org.jooq.DSLContext
import org.jooq.impl.DSL
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
class CardRepository(val dsl: DSLContext) {
    fun findAllByDeck(deck: String): Flux<Card> =
        Flux.from(
            dsl.select(
                Tables.CARDS.ID,
                Tables.CARDS.DECK,
                Tables.CARDS.ORDINAL,
                Tables.CARDS.VERSION,

                DSL.multiset(
                    DSL.select()
                        .from(Tables.CARD_DATA)
                        .where(Tables.CARD_DATA.CARD.eq(Tables.CARDS.ID))
                )
                    .`as`("data")
                    .convertFrom { it.into(com.benkyo.decks.data.CardData::class.java) },
            )
                .from(Tables.CARDS)
                .where(Tables.CARDS.DECK.eq(deck))
        )
            .map { it.into(Card::class.java) }

    fun deleteById(card: String) =
        Mono.from(
            dsl.deleteFrom(Tables.CARDS)
                .where(Tables.CARDS.ID.equal(card))
                .returningResult()
        ).map { it.into(Card::class.java) }

    fun save(card: Card): Mono<Card> = Mono.from(
        dsl.insertInto(Tables.CARDS)
            .columns()
            .onDuplicateKeyUpdate()
            .set(Tables.CARDS.ID, card.id)
            .set(Tables.CARDS.DECK, card.deck)
            .set(Tables.CARDS.ORDINAL, card.ordinal)
            .set(Tables.CARDS.VERSION, card.version)
            .returningResult()
    ).map { it.into(Card::class.java) }
}
