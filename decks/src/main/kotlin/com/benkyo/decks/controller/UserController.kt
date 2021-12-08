package com.benkyo.decks.controller

import com.benkyo.decks.data.User
import com.benkyo.decks.repository.UserRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ServerWebExchange
import java.security.Principal

@RestController
@RequestMapping("/users")
class UserController(
    private val repository: UserRepository
) {
    @GetMapping("/@me")
    suspend fun getSelf(
        exchange: ServerWebExchange,
        principal: Principal
    ): User? = repository.findById(principal.name) ?: User(principal.name, 0, null)
}
