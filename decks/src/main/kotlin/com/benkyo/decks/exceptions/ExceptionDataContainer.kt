package com.benkyo.decks.exceptions

import com.fasterxml.jackson.annotation.JsonInclude
import java.io.Serializable

data class ExceptionDataContainer(
    val message: String,
    val messageKey: String,
    val errorCode: Int,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val replacements: Array<Serializable>,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val entities: Map<String, Serializable>
) {
    // region: Generated functions
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ExceptionDataContainer

        if (message != other.message) return false
        if (messageKey != other.messageKey) return false
        if (errorCode != other.errorCode) return false
        if (!replacements.contentEquals(other.replacements)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = message.hashCode()
        result = 31 * result + messageKey.hashCode()
        result = 31 * result + errorCode
        result = 31 * result + replacements.contentHashCode()
        return result
    }
    // endregion
}

fun ExceptionDataContainer(exception: JSONException): ExceptionDataContainer {
    return ExceptionDataContainer(
        message = exception.message,
        messageKey = exception.messageKey,
        errorCode = exception.errorCode,
        replacements = exception.replacements,
        entities = exception.entities
    )
}
