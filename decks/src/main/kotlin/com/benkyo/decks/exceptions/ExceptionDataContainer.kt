package com.benkyo.decks.exceptions

import com.fasterxml.jackson.annotation.JsonInclude
import java.io.Serializable

data class ExceptionDataContainer(
    val errorCode: Int,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val entities: Map<String, Serializable>
) {
    // region: Generated functions
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ExceptionDataContainer

        if (errorCode != other.errorCode) return false

        return true
    }

    override fun hashCode(): Int =
        errorCode
    // endregion
}

fun ExceptionDataContainer(exception: JSONException): ExceptionDataContainer {
    return ExceptionDataContainer(
        errorCode = exception.errorCode,
        entities = exception.entities
    )
}
