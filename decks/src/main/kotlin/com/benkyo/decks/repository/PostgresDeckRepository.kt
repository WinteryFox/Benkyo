package com.benkyo.decks.repository

import com.benkyo.decks.data.Card
import com.benkyo.decks.data.DeckData
import org.springframework.data.r2dbc.convert.R2dbcConverter
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.r2dbc.core.bind
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
class PostgresDeckRepository(
    private val client: DatabaseClient,
    private val cardController: CardController,
    private val converter: R2dbcConverter
) : DeckRepository {
    override fun getAll(): Flux<DeckData> =
        client.sql("SELECT * FROM decks")
            .map { row, metadata -> converter.read(DeckData::class.java, row, metadata) }
            .all()

    override fun getById(id: String): Mono<DeckData> =
        client.sql("SELECT * FROM decks WHERE id = $1")
            .bind(0, id)
            .map { row, metadata -> converter.read(DeckData::class.java, row, metadata) }
            .first()

    override fun save(data: DeckData): Mono<Void> =
        client.sql("INSERT INTO decks (id, author, name, description, source_language, target_language, image_hash) VALUES ($1, $2, $3, $4, $5, $6, $7)")
            .bind(0, data.id)
            .bind(1, data.author)
            .bind(2, data.name)
            .bind(3, data.description)
            .bind(4, data.sourceLanguage)
            .bind(5, data.targetLanguage)
            .bind(6, data.imageHash)
            .then()

    override fun delete(id: String): Mono<Void> =
        client.sql("DELETE FROM decks WHERE id = $1")
            .bind(0, id)
            .then()

    override fun getCards(id: String): Flux<Card> = cardController.findAllByDeck(id)
}
