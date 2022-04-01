package com.benkyo.decks

import net.lecousin.reactive.data.relational.LcReactiveDataRelationalInitializer
import net.lecousin.reactive.data.relational.postgres.PostgresConfiguration
import net.lecousin.reactive.data.relational.repository.LcR2dbcRepositoryFactoryBean
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Import
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories

@SpringBootApplication
@EnableR2dbcRepositories(repositoryFactoryBeanClass = LcR2dbcRepositoryFactoryBean::class)
@Import(PostgresConfiguration::class)
class Decks

fun main() {
    LcReactiveDataRelationalInitializer.init()
    runApplication<Decks>()
}
