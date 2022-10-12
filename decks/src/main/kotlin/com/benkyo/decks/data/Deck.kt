package com.benkyo.decks.data

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Version
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("decks")
data class Deck(
    @Id
    val id: String,
    val author: String,
    val isPrivate: Boolean,
    val createdAt: LocalDateTime,
    val name: String,
    val shortDescription: String,
    val description: String,
    val sourceLanguage: String,
    val targetLanguage: String,
    val imageHash: String?,
    val tags: Array<String>,

    @JsonIgnore
    @Version
    val version: Int = 0
) {
    // region: Generated functions
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Deck

        if (id != other.id) return false
        if (author != other.author) return false
        if (isPrivate != other.isPrivate) return false
        if (createdAt != other.createdAt) return false
        if (name != other.name) return false
        if (shortDescription != other.shortDescription) return false
        if (description != other.description) return false
        if (sourceLanguage != other.sourceLanguage) return false
        if (targetLanguage != other.targetLanguage) return false
        if (imageHash != other.imageHash) return false
        if (!tags.contentEquals(other.tags)) return false
        if (version != other.version) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + author.hashCode()
        result = 31 * result + isPrivate.hashCode()
        result = 31 * result + createdAt.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + shortDescription.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + sourceLanguage.hashCode()
        result = 31 * result + targetLanguage.hashCode()
        result = 31 * result + (imageHash?.hashCode() ?: 0)
        result = 31 * result + tags.contentHashCode()
        result = 31 * result + version
        return result
    }
    // endregion
}
