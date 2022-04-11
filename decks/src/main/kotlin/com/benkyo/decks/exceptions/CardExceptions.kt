package com.benkyo.decks.exceptions

import com.benkyo.decks.ErrorCodes

class CardNotFoundException(card: String) : NotFoundException(
    "Card not found",
    "card-not-found",
    ErrorCodes.CARD_NOT_FOUND,
    arrayOf(card),
    mapOf("card" to card)
)
