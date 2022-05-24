package com.benkyo.decks.exceptions

import com.benkyo.decks.ErrorCodes

class UnsupportedMimeTypeException(
    mimeType: String
) : BadRequestException(
    ErrorCodes.UNSUPPORTED_MIME_TYPE,
    mapOf("image" to mimeType)
)

class FileTooLargeException(
    size: Int
): BadRequestException(
    ErrorCodes.FILE_TOO_LARGE,
    mapOf("image" to hashMapOf("size" to size))
)

class BadDimensionsException(
    width: Int,
    height: Int
) : BadRequestException(
    ErrorCodes.BAD_DIMENSIONS,
    mapOf("image" to hashMapOf("width" to width, "height" to height))
)
