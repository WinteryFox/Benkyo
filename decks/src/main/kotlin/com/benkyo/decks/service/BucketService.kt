package com.benkyo.decks.service

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.stereotype.Service
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.GetObjectRequest
import software.amazon.awssdk.services.s3.model.GetObjectResponse
import software.amazon.awssdk.services.s3.model.PutObjectRequest

@Service
class BucketService(
    region: Region = Region.EU_CENTRAL_1,
    private val bucket: String = "app-benkyo"
) {
    private val client = S3Client.builder()
        .region(region)
        .build()

    // TODO: This eats exceptions regardless of what type they were, should probably eat only 404's
    suspend fun <T> get(
        key: String,
        bucket: String = this.bucket,
        transform: (data: ByteArray?, response: GetObjectResponse?) -> T?
    ): T? {
        val res = runCatching {
            client.getObject(GetObjectRequest.builder().bucket(bucket).key(key).build())
        }.getOrNull()

        return transform(
            withContext(Dispatchers.IO) {
                res?.readAllBytes()
            },
            res?.response()
        )
    }

    suspend fun put(
        key: String,
        data: ByteArray,
        bucket: String = this.bucket
    ) = client.putObject(PutObjectRequest.builder().bucket(bucket).key(key).build(), RequestBody.fromBytes(data))
}
