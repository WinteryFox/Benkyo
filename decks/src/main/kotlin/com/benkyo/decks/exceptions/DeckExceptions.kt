package com.benkyo.decks.exceptions

import com.benkyo.decks.ErrorCodes

class DeckNotFoundException(deck: String) : NotFoundException(
    ErrorCodes.DECK_NOT_FOUND,
    mapOf("deck" to deck)
)
