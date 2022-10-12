package com.benkyo.decks

import io.r2dbc.spi.ConnectionFactory
import org.jooq.DSLContext
import org.jooq.SQLDialect
import org.jooq.conf.Settings
import org.jooq.impl.DSL
import org.jooq.impl.DefaultConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class JooqConfig(val cfi: ConnectionFactory) {
    @Bean
    fun jOOQDSLContext(): DSLContext = DSL.using(jOOQConfiguration()).dsl()

    @Bean
    fun jOOQConfiguration(): DefaultConfiguration {
        val config = DefaultConfiguration()
        val settings = Settings()

        settings.isMapConstructorParameterNamesInKotlin = true

        config.set(SQLDialect.POSTGRES)
        config.set(settings)
        config.set(cfi)

        return config
    }
}
