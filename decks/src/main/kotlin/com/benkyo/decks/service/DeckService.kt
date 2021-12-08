package com.benkyo.decks.service

import com.benkyo.decks.`object`.Deck
import com.benkyo.decks.repository.DeckRepository
import org.springframework.stereotype.Service

@Service
class DeckService(
    val repository: DeckRepository
) {
    fun getAll() = repository.getAll().map { Deck(it) }

    fun getById(id: String) = repository.getById(id).map { Deck(it) }

    fun getCards(id: String) = repository.getCards(id)
}
