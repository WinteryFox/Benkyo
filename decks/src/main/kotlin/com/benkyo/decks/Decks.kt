package com.benkyo.decks

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Import

@SpringBootApplication
@Import(JooqConfig::class)
class Decks

fun main() {
    runApplication<Decks>()
}
