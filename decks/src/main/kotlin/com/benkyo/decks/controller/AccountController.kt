package com.benkyo.decks.controller

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/register")
class AccountController {
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @PostMapping(consumes = ["application/json"], produces = ["application/json"])
    fun base(@RequestBody data: String): String {
        println(data)
        return "{}"
    }
}
