package com.benkyo.decks.controller

import com.ibm.icu.util.ULocale
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/languages")
class LocaleController {
    @GetMapping
    fun getLocales(
        l: java.util.Locale,
        @RequestParam
        locale: String?
    ): List<Locale> =
        ULocale.getAvailableLocales()
            .filter { it.baseName != it.language }
            .map {
                Locale(
                    it.toLanguageTag(),
                    it.getDisplayNameWithDialect(
                        ULocale.forLanguageTag(locale ?: l.toLanguageTag())
                    )
                )
            }
}

data class Locale(
    val code: String,
    val name: String
)
