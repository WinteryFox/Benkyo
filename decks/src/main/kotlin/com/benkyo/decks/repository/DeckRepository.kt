package com.benkyo.decks.repository

import com.benkyo.decks.data.Deck
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface DeckRepository : CoroutineCrudRepository<Deck, String>
