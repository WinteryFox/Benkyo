package com.benkyo.decks

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.provisioning.InMemoryUserDetailsManager

@TestConfiguration
class SpringSecurityWebAuxTestConfig {
    @Bean
    @Primary
    fun userDetailsService(): UserDetailsService {
        return InMemoryUserDetailsManager(

        )
    }
}
