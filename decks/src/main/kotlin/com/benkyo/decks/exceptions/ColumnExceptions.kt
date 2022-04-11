package com.benkyo.decks.exceptions

import com.benkyo.decks.ErrorCodes

class ColumnsNotFound(columns: Array<String>) : BadRequestException(
    "Columns not found",
    "columns-not-found",
    ErrorCodes.CARD_NOT_FOUND,
    arrayOf(columns.joinToString()),
    mapOf("columns" to columns)
)
