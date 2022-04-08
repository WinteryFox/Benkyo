package com.benkyo.decks.repository

import com.benkyo.decks.data.IgnoredCard
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface IgnoredCardRepository : CoroutineCrudRepository<IgnoredCard, String>
