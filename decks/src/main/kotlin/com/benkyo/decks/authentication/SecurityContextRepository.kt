package com.benkyo.decks.authentication

import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import kotlinx.coroutines.reactor.mono
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextImpl
import org.springframework.security.web.server.context.ServerSecurityContextRepository
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class SecurityContextRepository(
    val authenticationManager: ReactiveAuthenticationManager
) : ServerSecurityContextRepository {
    override fun save(exchange: ServerWebExchange, context: SecurityContext): Mono<Void> =
        throw UnsupportedOperationException()

    override fun load(exchange: ServerWebExchange): Mono<SecurityContext> = mono {
        val header = exchange.request.headers.getFirst(HttpHeaders.AUTHORIZATION) ?: return@mono null
        if (!header.startsWith("Bearer "))
            return@mono null

        val token = header.substring(7)

        return@mono SecurityContextImpl(
            authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(
                    token,
                    token
                )
            ).awaitSingleOrNull() ?: return@mono null
        )
    }
}
