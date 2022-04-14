package com.benkyo.decks.exceptions

import com.benkyo.decks.ErrorCodes

class InvalidLocaleException(locale: String) : BadRequestException(
    ErrorCodes.INVALID_LOCALE_CODE,
    mapOf("locale" to locale)
)
