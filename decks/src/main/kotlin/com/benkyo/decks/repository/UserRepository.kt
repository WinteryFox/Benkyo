package com.benkyo.decks.repository

import com.benkyo.decks.data.User
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface UserRepository : CoroutineCrudRepository<User, String>
