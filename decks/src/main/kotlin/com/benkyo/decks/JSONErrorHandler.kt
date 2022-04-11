package com.benkyo.decks

import com.benkyo.decks.exceptions.BadRequestException
import com.benkyo.decks.exceptions.ExceptionDataContainer
import com.benkyo.decks.exceptions.NotFoundException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class JSONErrorHandler {
    @ResponseBody
    @ExceptionHandler(NotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleNotFoundException(exception: NotFoundException): ExceptionDataContainer {
        return ExceptionDataContainer(exception)
    }

    @ResponseBody
    @ExceptionHandler(BadRequestException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleNotFoundException(exception: BadRequestException): ExceptionDataContainer {
        return ExceptionDataContainer(exception)
    }
}
