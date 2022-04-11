package com.benkyo.decks.exceptions

import com.benkyo.decks.ErrorCodes

class DeckNotFoundException(deck: String) : NotFoundException(
    "Deck not found",
    "deck-not-found",
    ErrorCodes.DECK_NOT_FOUND,
    arrayOf(deck),
    mapOf("deck" to deck)
)
