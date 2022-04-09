package com.benkyo.decks.utils

import org.jooq.Record

/**
 * Get a field's value by String name, returning `null` if it can't be found.
 */
fun Record.getOrNull(key: String): Any? = try {
    get(key)
} catch (e: IllegalArgumentException) {
    null
}

/**
 * Get a field's value by String name, returning [default] if it can't be found.
 */
inline fun <reified T> Record.getOrDefault(key: String, default: T) = try {
    get(key)
} catch (e: IllegalArgumentException) {
    default
}
