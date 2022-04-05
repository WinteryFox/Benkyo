package com.benkyo.decks.repository

import com.benkyo.decks.data.CardData
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface CardDataRepository : CoroutineCrudRepository<CardData, String>
