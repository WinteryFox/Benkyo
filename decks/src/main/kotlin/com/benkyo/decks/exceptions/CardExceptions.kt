package com.benkyo.decks.exceptions

import com.benkyo.decks.ErrorCodes

class CardNotFoundException(card: String) : NotFoundException(
    ErrorCodes.CARD_NOT_FOUND,
    mapOf("card" to card)
)
