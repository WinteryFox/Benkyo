package com.benkyo.decks.utils

import org.apache.commons.lang3.LocaleUtils
import java.util.*

/**
 * Given a [String], return a [Boolean] representing whether the [String] contains a valid locale code.
 *
 * @return `true` if the [String] is a valid locale code, `false` otherwise.
 */
fun String.isValidLocaleCode(): Boolean {
    val locale = Locale.Builder().setLanguageTag(this).build()

    return LocaleUtils.isAvailableLocale(locale)
}
