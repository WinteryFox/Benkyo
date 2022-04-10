package com.benkyo.decks

import com.benkyo.decks.data.Deck
import com.benkyo.decks.repository.DeckRepository
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.times
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import java.time.LocalDateTime

@SpringBootTest
class DeckTest(
    @MockBean
    private val repository: DeckRepository
) {
    @Test
    suspend fun testCardRepository() {
        // TODO Not sure what needs doing here? -- Gareth

        val deck = Deck(
            author = "0",
            createdAt = LocalDateTime.now(),
            description = "This is a longer description supporting **Markdown**",
            id = "0",
            imageHash = null,
            isPrivate = false,
            name = "Test Deck",
            shortDescription = "This is a test description.",
            sourceLanguage = "en-US",
            targetLanguage = "ja-JP",
            version = 0
        )

        Mockito.`when`(repository.save(deck)).thenReturn(null)
        Mockito.verify(repository, times(1)).save(deck)
    }
}
