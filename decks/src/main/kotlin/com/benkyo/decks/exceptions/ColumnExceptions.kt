package com.benkyo.decks.exceptions

import com.benkyo.decks.ErrorCodes

class ColumnsNotFound(columns: Array<String>) : BadRequestException(
    ErrorCodes.CARD_NOT_FOUND,
    mapOf("columns" to columns)
)
