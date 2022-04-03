package com.benkyo.decks

import io.r2dbc.spi.ConnectionFactory
import org.jooq.DSLContext
import org.jooq.impl.DSL
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class JooqConfig (val cfi: ConnectionFactory) {
    @Bean
    fun jOOQDSLContext(): DSLContext = DSL.using(cfi).dsl()
}
