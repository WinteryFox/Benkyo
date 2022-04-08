package com.benkyo.decks.repository

import com.benkyo.decks.data.Attachment
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface AttachmentRepository : CoroutineCrudRepository<Attachment, String>
