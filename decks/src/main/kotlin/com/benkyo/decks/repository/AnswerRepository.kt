package com.benkyo.decks.repository

import com.benkyo.decks.data.Answer
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

@JvmDefaultWithoutCompatibility
interface AnswerRepository : CoroutineCrudRepository<Answer, String> {
    suspend fun deleteAllByCard(card: String)
}
