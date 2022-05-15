package com.benkyo.decks.controller

import aws.smithy.kotlin.runtime.content.toByteArray
import com.benkyo.decks.service.BucketService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ServerWebExchange

@RestController
@RequestMapping("/assets")
class BucketController(
    private val bucket: BucketService
) {
    @GetMapping("/decks/{asset}")
    suspend fun getDeckImage(
        exchange: ServerWebExchange,
        @PathVariable asset: String
    ): ByteArray? {
        return bucket.getObject(asset) {
            if (it.body == null) {
                exchange.response.statusCode = HttpStatus.NOT_FOUND
                return@getObject null
            }

            exchange.response.headers.set("Content-Type", it.contentType)
            exchange.response.headers.set("Content-Length", it.contentLength.toString())
            return@getObject it.body!!.toByteArray()
        }
    }
}
