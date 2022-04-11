package com.benkyo.decks.exceptions

import com.benkyo.decks.ErrorCodes
import java.io.Serializable

sealed class JSONException(
    override val message: String,
    val messageKey: String,
    val errorCode: Int,
    val replacements: Array<Serializable>,
    val entities: Map<String, Serializable> = mapOf()
) : RuntimeException()

open class NotFoundException(
    message: String = "Not found",
    messageKey: String = "not-found",
    errorCode: Int,
    replacements: Array<Serializable>,
    entities: Map<String, Serializable> = mapOf()
) : JSONException(message, messageKey, errorCode, replacements, entities)

open class BadRequestException(
    message: String = "Bad request",
    messageKey: String = "bad-request",
    errorCode: Int,
    replacements: Array<Serializable>,
    entities: Map<String, Serializable> = mapOf()
) : JSONException(message, messageKey, errorCode, replacements, entities)

open class UnauthorizedException(
    message: String = "Unauthorized",
    messageKey: String = "unauthorized",
    errorCode: Int = ErrorCodes.UNAUTHORIZED,
    replacements: Array<Serializable> = arrayOf(),
    entities: Map<String, Serializable> = mapOf()
) : JSONException(message, messageKey, errorCode, replacements, entities)
