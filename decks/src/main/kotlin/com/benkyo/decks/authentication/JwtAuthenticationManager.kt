package com.benkyo.decks.authentication

import com.auth0.jwk.UrlJwkProvider
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import kotlinx.coroutines.reactor.mono
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
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
    private val jwt = JWT()
    private val keyFactory = KeyFactory.getInstance("RSA")

    override fun authenticate(authentication: Authentication): Mono<Authentication> = mono {
        val jwt = jwt.decodeJwt(authentication.credentials.toString())
        val jwk = provider.all.firstOrNull { it.id == jwt.keyId && it.algorithm == jwt.algorithm && it.algorithm == "RS256" }
            ?: return@mono null
        val verifier = JWT.require(
            Algorithm.RSA256(
                keyFactory.generatePublic(X509EncodedKeySpec(jwk.publicKey.encoded)) as RSAPublicKey,
                null
            )
        ).build()
        try {
            verifier.verify(authentication.credentials.toString())
        } catch (exception: RuntimeException) {
            exception.printStackTrace()
            return@mono null
        }

        return@mono UsernamePasswordAuthenticationToken(
            jwt.subject,
            null,
            emptyList()
        )
    }
}
