package com.benkyo.decks

object ErrorCodes {
    // The different types of entity we may need to have errors for
    private const val ENTITY_DECK = 1
    private const val ENTITY_CARD = 2

    // For some errors, we need to provide information on types of data that aren't entities
    private const val TYPE_LOCALE = 100

    // Type of error we're dealing with
    private const val ERROR_UNAUTHORIZED = 20000
    private const val ERROR_INVALID = 30000
    private const val ERROR_NOT_FOUND = 40000

    const val UNAUTHORIZED = ERROR_UNAUTHORIZED

    const val INVALID_LOCALE_CODE = ERROR_INVALID + TYPE_LOCALE

    const val DECK_NOT_FOUND = ERROR_NOT_FOUND + ENTITY_DECK
    const val CARD_NOT_FOUND = ERROR_NOT_FOUND + ENTITY_CARD
}
