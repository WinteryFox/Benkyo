package com.benkyo.decks.controller

import com.benkyo.decks.data.Deck
import com.benkyo.decks.exceptions.*
import com.benkyo.decks.repository.DeckRepository
import com.benkyo.decks.repository.UserRepository
import com.benkyo.decks.request.DeckCreateRequest
import com.benkyo.decks.request.DeckPatchRequest
import com.benkyo.decks.service.BucketService
import com.benkyo.decks.utils.isValidLocaleCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.withContext
import org.apache.commons.codec.digest.DigestUtils
import org.apache.tika.parser.html.DataURISchemeUtil
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ServerWebExchange
import java.io.ByteArrayInputStream
import java.security.Principal
import java.time.LocalDateTime
import java.util.*
import javax.imageio.ImageIO
import javax.validation.Valid

@RestController
@RequestMapping("/decks")
class DeckController(
    private val userRepository: UserRepository,
    private val deckRepository: DeckRepository,
    private val bucket: BucketService
) {
    private val imageMimeTypes = listOf(
        MediaType.IMAGE_GIF_VALUE,
        MediaType.IMAGE_JPEG_VALUE,
        MediaType.IMAGE_PNG_VALUE
    )

    @GetMapping
    suspend fun getDecks(): Flow<Deck> = deckRepository.findAll().filter { !it.isPrivate }

    @PostMapping(consumes = [APPLICATION_JSON_VALUE], produces = [APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.CREATED)
    suspend fun createDeck(
        exchange: ServerWebExchange,
        principal: Principal,
        @Valid @RequestBody request: DeckCreateRequest
    ): Deck? {
        userRepository.findById(principal.name)
            ?: throw UnauthorizedException()

        if (!request.sourceLanguage.isValidLocaleCode())
            throw InvalidLocaleException(request.sourceLanguage)
        if (!request.targetLanguage.isValidLocaleCode())
            throw InvalidLocaleException(request.targetLanguage)

        return deckRepository.save(
            Deck(
                id = UUID.randomUUID().toString(),
                author = principal.name,
                isPrivate = request.isPrivate,
                createdAt = LocalDateTime.now(),
                name = request.name,
                shortDescription = request.shortDescription,
                description = request.description,
                sourceLanguage = request.sourceLanguage,
                targetLanguage = request.targetLanguage,
                imageHash = null,
                tags = request.tags
            )
        )
    }

    @PatchMapping("/{id}", consumes = [APPLICATION_JSON_VALUE], produces = [APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.OK)
    suspend fun updateDeck(
        exchange: ServerWebExchange,
        principal: Principal,
        @PathVariable id: String,
        @Valid @RequestBody request: DeckPatchRequest
    ): Deck? {
        val user = userRepository.findById(principal.name)
            ?: throw UnauthorizedException()
        val deck = deckRepository.findById(id)
            ?: throw DeckNotFoundException(id)
        if (deck.author != user.id)
            throw UnauthorizedException()

        if (request.sourceLanguage != null && !request.sourceLanguage.isValidLocaleCode())
            throw InvalidLocaleException(request.sourceLanguage)
        if (request.targetLanguage != null && !request.targetLanguage.isValidLocaleCode())
            throw InvalidLocaleException(request.targetLanguage)

        var imageHash: String? = null
        if (request.image != null) {
            val dataUri = DataURISchemeUtil().parse(request.image)
            if (!dataUri.isBase64 || !imageMimeTypes.contains(dataUri.mediaType.toString()))
                throw UnsupportedMimeTypeException(dataUri.mediaType.toString())

            val data = withContext(Dispatchers.IO) {
                val data = dataUri.inputStream.readAllBytes()
                if (data.size > 32768)
                    throw FileTooLargeException(data.size)

                val image = ImageIO.read(ByteArrayInputStream(data))
                if (image.width != image.height && image.width > 512)
                    throw BadDimensionsException(image.width, image.height)

                // TODO: Downscale the image?
                return@withContext data
            }
            imageHash = DigestUtils.md5Hex(data)
            bucket.put(
                "decks/$imageHash",
                dataUri.mediaType.toString(),
                data
            )
        }

        return deckRepository.save(
            deck.copy(
                description = request.description ?: deck.description,
                isPrivate = request.isPrivate ?: deck.isPrivate,
                name = request.name ?: deck.name,
                shortDescription = request.shortDescription ?: deck.shortDescription,
                sourceLanguage = request.sourceLanguage ?: deck.sourceLanguage,
                targetLanguage = request.targetLanguage ?: deck.targetLanguage,
                imageHash = imageHash,
                tags = request.tags
            )
        )
    }

    @GetMapping("/{id}", produces = [APPLICATION_JSON_VALUE])
    suspend fun getDeck(
        principal: Principal,
        @PathVariable id: String
    ): Deck {
        val deck = deckRepository.findById(id) ?: throw DeckNotFoundException(id)
        if (deck.isPrivate && deck.author != principal.name)
            throw UnauthorizedException()

        return deck
    }

    @DeleteMapping("/{id}")
    suspend fun deleteDeck(
        principal: Principal,
        exchange: ServerWebExchange,
        @PathVariable id: String
    ) {
        val user = userRepository.findById(principal.name) ?: throw UnauthorizedException()
        val deck = deckRepository.findById(id) ?: throw DeckNotFoundException(id)
        if (deck.author != user.id)
            throw UnauthorizedException()

        deckRepository.deleteById(id)
    }
}
