package com.benkyo.decks.repository

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
    private val converter: R2dbcConverter
) : DeckRepository {
    override fun getAll(): Flux<DeckData> =
        client.sql("SELECT * FROM decks")
            .map { row, metadata -> converter.read(DeckData::class.java, row, metadata) }
            .all()

    override fun getById(id: Long): Mono<DeckData> =
        client.sql("SELECT * FROM decks WHERE id = $1")
            .bind(0, id)
            .map { row, metadata -> converter.read(DeckData::class.java, row, metadata) }
            .first()

    override fun save(deckData: DeckData): Mono<Void> =
        client.sql("INSERT INTO decks (id, author, name, source_language, target_language, image_hash) VALUES ($1, $2, $3, $4, $5, $6)")
            .bind(0, deckData.id)
            .bind(1, deckData.author)
            .bind(2, deckData.name)
            .bind(3, deckData.sourceLanguage)
            .bind(4, deckData.targetLanguage)
            .bind(5, deckData.imageHash)
            .then()

    override fun delete(id: Long): Mono<Void> =
        client.sql("DELETE FROM decks WHERE id = $1")
            .bind(0, id)
            .then()
}
