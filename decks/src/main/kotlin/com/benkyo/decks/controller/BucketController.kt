package com.benkyo.decks.controller

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
    ): ByteArray? = bucket.get("decks/$asset") { data, response ->
        if (data == null || response == null) {
            exchange.response.statusCode = HttpStatus.NOT_FOUND
            return@get null
        }

        exchange.response.headers.set("Content-Type", response.contentType())
        exchange.response.headers.set("Content-Length", response.contentLength().toString())
        return@get data
    }
}
