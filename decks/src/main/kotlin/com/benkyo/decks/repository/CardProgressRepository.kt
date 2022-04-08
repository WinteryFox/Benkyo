package com.benkyo.decks.repository

import com.benkyo.decks.data.CardProgress
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface CardProgressRepository : CoroutineCrudRepository<CardProgress, String>
