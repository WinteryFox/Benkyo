package com.benkyo.decks

object ErrorCodes {
    // The different types of entity we may need to have errors for
    private const val ENTITY_DECK = 1
    private const val ENTITY_CARD = 2

    // For some errors, we need to provide information on types of data that aren't entities
    private const val TYPE_LOCALE = 100
    private const val TYPE_MEDIA = 200

    // Type of error we're dealing with
    private const val ERROR_UNAUTHORIZED = 20000
    private const val ERROR_INVALID = 30000
    private const val ERROR_NOT_FOUND = 40000

    const val UNAUTHORIZED = ERROR_UNAUTHORIZED

    // Invalid error codes
    const val INVALID_LOCALE_CODE = ERROR_INVALID + TYPE_LOCALE
    const val UNSUPPORTED_MIME_TYPE = ERROR_INVALID + TYPE_MEDIA
    const val FILE_TOO_LARGE = ERROR_INVALID + TYPE_MEDIA + 1
    const val BAD_DIMENSIONS = ERROR_INVALID + TYPE_MEDIA + 2

    // Not found error codes
    const val DECK_NOT_FOUND = ERROR_NOT_FOUND + ENTITY_DECK
    const val CARD_NOT_FOUND = ERROR_NOT_FOUND + ENTITY_CARD
    const val FILE_NOT_FOUND = ERROR_NOT_FOUND + TYPE_MEDIA
}
