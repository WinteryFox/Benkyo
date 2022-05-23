package com.benkyo.decks.service

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.stereotype.Service
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.*

@Service
class BucketService(
    region: Region = Region.EU_CENTRAL_1,
    private val bucket: String = "app-benkyo"
) {
    private val client = S3Client.builder()
        .region(region)
        .build()

    suspend fun <T> get(
        key: String,
        bucket: String = this.bucket,
        transform: (data: ByteArray?, response: GetObjectResponse?) -> T?
    ): T? = try {
        val o = client.getObject(GetObjectRequest.builder().bucket(bucket).key(key).build())
        transform(
            withContext(Dispatchers.IO) {
                o.readAllBytes()
            },
            o.response()
        )
    } catch (_: NoSuchKeyException) {
        transform(null, null)
    } catch (e: S3Exception) {
        throw e
    }

    suspend fun put(
        key: String,
        data: ByteArray,
        bucket: String = this.bucket
    ) = client.putObject(PutObjectRequest.builder().bucket(bucket).key(key).build(), RequestBody.fromBytes(data))
}
