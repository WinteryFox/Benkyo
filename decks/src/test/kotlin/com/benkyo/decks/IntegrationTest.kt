package com.benkyo.decks

import com.benkyo.decks.data.Deck
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.boot.test.web.client.postForEntity
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class IntegrationTest(
    @Autowired
    private val testRestTemplate: TestRestTemplate
) {
    @Test
    fun testCreateDeck() {
        val deck = testRestTemplate.postForEntity<Deck>("http://localhost:8282/api/decks")
        assertTrue(deck.statusCode == HttpStatus.UNAUTHORIZED)
        assertNull(deck.body)
    }

    @Test
    fun testGetAllDecks() {
        val responseDeck: ResponseEntity<Array<Deck>> = testRestTemplate.getForEntity("http://localhost:8282/api/decks")
        assertTrue(responseDeck.statusCode == HttpStatus.OK)
        assertNotNull(responseDeck.body)
    }
}
