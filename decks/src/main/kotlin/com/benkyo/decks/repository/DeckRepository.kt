package com.benkyo.decks.repository

import com.benkyo.decks.data.Card
import com.benkyo.decks.data.DeckData
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface DeckRepository {
    fun getAll(): Flux<DeckData>

    fun getById(id: Long): Mono<DeckData>

    fun save(data: DeckData): Mono<Void>

    fun delete(id: Long): Mono<Void>

    fun getCards(id: Long): Flux<Card>
}
