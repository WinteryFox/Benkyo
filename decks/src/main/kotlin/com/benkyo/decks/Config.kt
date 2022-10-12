package com.benkyo.decks

import kotlinx.coroutines.reactor.mono
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.context.ServerSecurityContextRepository
import org.springframework.web.cors.CorsConfiguration

@Configuration
@EnableWebFluxSecurity
class Config(
    private val authenticationManager: ReactiveAuthenticationManager,
    private val securityContextRepository: ServerSecurityContextRepository
) {
    @Bean
    fun configure(httpSecurity: ServerHttpSecurity): SecurityWebFilterChain =
        httpSecurity
            .exceptionHandling {
                it.authenticationEntryPoint { exchange, _ ->
                    mono {
                        exchange.response.statusCode = HttpStatus.UNAUTHORIZED
                        return@mono null
                    }
                }
                it.accessDeniedHandler { exchange, _ ->
                    mono {
                        exchange.response.statusCode = HttpStatus.FORBIDDEN
                        return@mono null
                    }
                }
            }
            .authenticationManager(authenticationManager)
            .securityContextRepository(securityContextRepository)
            .formLogin().disable()
            .cors().configurationSource {
                CorsConfiguration().apply {
                    allowedOriginPatterns = listOf("*")
                    allowCredentials = true
                    allowedMethods = listOf("*")
                    allowedHeaders = listOf("*")
                }
            }.and()
            .csrf().disable()
            .httpBasic().disable()
            .authorizeExchange()
            .pathMatchers(HttpMethod.GET, "/decks/**").permitAll()
            .pathMatchers(HttpMethod.GET, "/languages").permitAll()
            .pathMatchers(HttpMethod.GET, "/assets/**").permitAll()
            .pathMatchers(HttpMethod.POST, "/register").permitAll() // TODO: TEMPORARY
            .anyExchange().authenticated().and()
            .build()
}
