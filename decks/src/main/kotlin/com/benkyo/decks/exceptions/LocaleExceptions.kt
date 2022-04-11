package com.benkyo.decks.exceptions

import com.benkyo.decks.ErrorCodes

class InvalidLocaleException(locale: String) : BadRequestException(
    "Invalid locale provided",
    "invalid-locale",
    ErrorCodes.INVALID_LOCALE_CODE,
    arrayOf(locale),
    mapOf("locale" to locale)
)
