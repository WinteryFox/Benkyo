package com.benkyo.decks.authentication

import com.auth0.jwk.UrlJwkProvider
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTDecodeException
import com.auth0.jwt.exceptions.JWTVerificationException
import kotlinx.coroutines.reactor.mono
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.net.URL
import java.security.KeyFactory
import java.security.interfaces.RSAPublicKey
import java.security.spec.X509EncodedKeySpec

@Component
class JwtAuthenticationManager : ReactiveAuthenticationManager {
    private val provider =
        UrlJwkProvider(URL("https://cognito-idp.us-east-2.amazonaws.com/us-east-2_3MQKXi2A6/.well-known/jwks.json"))
    private val client = JWT()
    private val keyFactory = KeyFactory.getInstance("RSA")
    private val verifiers = provider.all.associate {
        it.id to
            JWT.require(
                Algorithm.RSA256(
                    keyFactory.generatePublic(X509EncodedKeySpec(it.publicKey.encoded)) as RSAPublicKey,
                    null
                )
            ).build()
    }

    override fun authenticate(authentication: Authentication): Mono<Authentication> = mono {
        try {
            val jwt =
                verifiers[client.decodeJwt(authentication.credentials.toString()).id]
                    ?.verify(authentication.credentials.toString())
                    ?: return@mono null

            return@mono UsernamePasswordAuthenticationToken(
                jwt.subject,
                null,
                emptyList()
            )
        } catch (exception: JWTVerificationException) {
            return@mono null
        } catch (exception: JWTDecodeException) {
            return@mono null
        }
    }
}
