package com.benkyo.decks.repository

import com.benkyo.decks.data.Attachment
import com.benkyo.decks.data.Card
import com.benkyo.decks.data.CardData
import com.benkyo.decks.jooq.Tables
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import org.jooq.DSLContext
import org.jooq.impl.DSL
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
class CardRepository(val dsl: DSLContext) {
    fun get(deck: String, card: String): Mono<Card?> =
        Mono.from(
            dsl.select(
                Tables.CARDS.ID,
                Tables.CARDS.DECK,
                Tables.CARDS.ORDINAL,
                Tables.CARDS.VERSION,
            )
                .from(Tables.CARDS)
                .where(
                    Tables.CARDS.DECK.eq(deck).and(
                        Tables.CARDS.ID.eq(card)
                    )
                )
        ).map { Card(it) }

    fun findAllByDeck(deck: String): Flow<Card> =
        Flux.from(
            dsl.select(
                Tables.CARDS.ID,
                Tables.CARDS.DECK,
                Tables.CARDS.ORDINAL,
                Tables.CARDS.VERSION,
                Tables.CARDS.TAGS,

                // Select the card data and add it to the Card object under the `data` prop
                DSL.multiset(
                    DSL.select()
                        .from(Tables.CARD_DATA)
                        .where(Tables.CARD_DATA.CARD.eq(Tables.CARDS.ID)),
                )
                    .`as`("data")
                    .convertFrom { it.into(CardData::class.java) },

                // Select the attachments, omitting the pivot table and storing under the `attachments` prop
                DSL.multiset(
                    DSL.select(
                        Tables.ATTACHMENTS.ID,
                        Tables.ATTACHMENTS.HASH,
                        Tables.ATTACHMENTS.MIME
                    )
                        .from(Tables.CARD_ATTACHMENTS)
                        .fullOuterJoin(Tables.ATTACHMENTS)
                        .on(Tables.ATTACHMENTS.ID.eq(Tables.CARD_ATTACHMENTS.ATTACHMENT))
                        .where(Tables.CARD_ATTACHMENTS.CARD.eq(Tables.CARDS.ID))
                )
                    .`as`("attachments")
                    .convertFrom { it.into(Attachment::class.java) },
            )
                .from(Tables.CARDS)
                .where(Tables.CARDS.DECK.eq(deck))
        )
            .map { Card(it) }
            .asFlow()

    fun deleteById(card: String) =
        Mono.from(
            dsl.deleteFrom(Tables.CARDS)
                .where(Tables.CARDS.ID.equal(card))
                .returningResult(
                    Tables.CARDS.ID,
                    Tables.CARDS.DECK,
                    Tables.CARDS.ORDINAL,
                    Tables.CARDS.VERSION,
                )
        ).map { Card(it) }

    fun deleteByDeck(deck: String) = Flux.from(
        dsl.deleteFrom(Tables.CARDS)
            .where(Tables.CARDS.DECK.eq(deck))
            .returningResult(
                Tables.CARDS.ID,
                Tables.CARDS.DECK,
                Tables.CARDS.ORDINAL,
                Tables.CARDS.VERSION,
            )
    )
        .map { Card(it) }
        .asFlow()

    fun save(card: Card): Mono<Card> = Mono.from(
        dsl.insertInto(Tables.CARDS)
            .columns()
            .onDuplicateKeyUpdate()
            .set(Tables.CARDS.ID, card.id)
            .set(Tables.CARDS.DECK, card.deck)
            .set(Tables.CARDS.ORDINAL, card.ordinal)
            .set(Tables.CARDS.VERSION, card.version)
            .returningResult(
                Tables.CARDS.ID,
                Tables.CARDS.DECK,
                Tables.CARDS.ORDINAL,
                Tables.CARDS.VERSION,
            )
    ).map { Card(it) }
}
