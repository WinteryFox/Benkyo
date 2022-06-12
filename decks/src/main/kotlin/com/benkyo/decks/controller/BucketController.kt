package com.benkyo.decks.controller

import com.benkyo.decks.exceptions.FileNotFoundException
import com.benkyo.decks.bucket.BucketService
import org.springframework.http.HttpHeaders
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
    ): ByteArray = bucket.get("decks/$asset") { data, response ->
        if (data == null || response == null)
            throw FileNotFoundException(asset)

        exchange.response.headers.set(HttpHeaders.CONTENT_TYPE, response.contentType())
        exchange.response.headers.set(HttpHeaders.CONTENT_LENGTH, response.contentLength().toString())
        return@get data
    }!!
}
