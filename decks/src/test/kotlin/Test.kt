import com.benkyo.decks.controller.DeckController
import com.benkyo.decks.data.DeckData
import com.benkyo.decks.repository.DeckRepository
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.mockito.Mockito.times
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters
import java.time.LocalDateTime

@ExtendWith(SpringExtension::class)
@WebFluxTest(controllers = [DeckController::class])
@Import(DeckRepository::class)
class Test(
    @MockBean
    private val repository: DeckRepository,
    private val webClient: WebTestClient
) {
    @Test
    suspend fun testCardRepository() {
        // TODO
        val deck = DeckData(
            "0",
            "0",
            false,
            "Test Deck",
            "This is a test description.",
            "en-US",
            "ja-JP",
            LocalDateTime.now(),
            null
        )

        Mockito.`when`(repository.save(deck)).thenReturn(null)
        webClient.post()
            .uri("/decks")
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(deck))
            .exchange()
            .expectStatus()
            .isCreated
        Mockito.verify(repository, times(1)).save(deck).awaitSingleOrNull()
    }
}
