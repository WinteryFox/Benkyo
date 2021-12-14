package com.benkyo.decks.repository

import com.benkyo.decks.data.CardProgress
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import reactor.core.publisher.Flux

interface CardProgressRepository : CoroutineCrudRepository<CardProgress, String> {
    fun findByDeckAndUser(deck: String, user: String): Flux<CardProgress>
}
