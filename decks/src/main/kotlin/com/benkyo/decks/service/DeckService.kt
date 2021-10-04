package com.benkyo.decks.service

import com.benkyo.decks.repository.DeckRepository
import org.springframework.stereotype.Service

@Service
class DeckService(
    val repository: DeckRepository
) {

}
