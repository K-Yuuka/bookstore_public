package com.quodigital.recruit.codetest.bookstore.api.handler

import com.quodigital.recruit.codetest.bookstore.api.generated.model.Error
import com.quodigital.recruit.codetest.bookstore.exception.ConflictException
import com.quodigital.recruit.codetest.bookstore.exception.NotFoundException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.lang.RuntimeException

/**
 * 例外処理クラス
 */
@RestControllerAdvice
class ExceptionHandler {
    /**
     * HTTP400
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException::class)
    public fun handleIllegalArgumentException(exception : IllegalArgumentException) : Error {
        return buildError(exception)
    }

    /**
     * HTTP404
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException::class)
    public fun handleNotFoundException(exception : NotFoundException) : Error {
        return buildError(exception)
    }

    /**
     * HTTP409
     */
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(ConflictException::class)
    public fun handleConflictException(exception : ConflictException) : Error {
        return buildError(exception)
    }

    /**
     * HTTP500
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception::class)
    public fun handleException(exception : Exception) : Error {
        return Error(message = exception.message)
    }

    private fun buildError(exception: RuntimeException) : Error {
        return Error(message = exception.message)
    }
}