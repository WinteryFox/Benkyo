package com.benkyo.decks.repository

import com.benkyo.decks.data.Column
import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ColumnRepository : CoroutineCrudRepository<Column, String> {
    fun findAllByDeck(deck: String): Flow<Column>
}
