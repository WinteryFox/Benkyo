package com.benkyo.decks.service

import aws.sdk.kotlin.services.s3.S3Client
import aws.sdk.kotlin.services.s3.model.GetObjectRequest
import aws.sdk.kotlin.services.s3.model.GetObjectResponse
import aws.sdk.kotlin.services.s3.model.PutObjectRequest
import aws.smithy.kotlin.runtime.content.ByteStream
import org.springframework.stereotype.Service

const val BUCKET = "app-benkyo"
const val REGION = "eu-central-1"

@Service
class BucketService {
    private val client = S3Client {
        region = REGION
    }

    suspend fun <T> getObject(
        key: String,
        transform: suspend (response: GetObjectResponse) -> T
    ): T = client.getObject(GetObjectRequest {
        this.key = key
        this.bucket = BUCKET
    }) {
        transform(it)
    }

    suspend fun putObject(key: String, body: ByteStream) {
        client.putObject {
            this.key = key
            this.bucket = BUCKET
            this.body = body
        }
    }
}
