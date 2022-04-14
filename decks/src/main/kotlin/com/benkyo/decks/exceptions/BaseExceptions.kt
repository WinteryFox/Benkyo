package com.benkyo.decks.exceptions

import com.benkyo.decks.ErrorCodes
import java.io.Serializable

sealed class JSONException(
    val errorCode: Int,
    val entities: Map<String, Serializable> = mapOf()
) : RuntimeException()

open class NotFoundException(
    errorCode: Int,
    entities: Map<String, Serializable> = mapOf()
) : JSONException(errorCode, entities)

open class BadRequestException(
    errorCode: Int,
    entities: Map<String, Serializable> = mapOf()
) : JSONException(errorCode, entities)

open class UnauthorizedException(
    errorCode: Int = ErrorCodes.UNAUTHORIZED,
    entities: Map<String, Serializable> = mapOf()
) : JSONException(errorCode, entities)
