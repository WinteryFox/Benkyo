package com.benkyo.decks

import java.nio.charset.Charset
import kotlin.reflect.KProperty

object Sql {
    operator fun getValue(thing: Sql, property: KProperty<*>): String {
        val resource = Sql::class.java.getResource("/sql/${property.name}.sql")!!

        return resource.readText(Charset.forName("UTF-8"))
    }

    val getCardsWithData by this
}
