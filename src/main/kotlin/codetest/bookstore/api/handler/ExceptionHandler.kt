package codetest.bookstore.api.handler

import codetest.bookstore.api.exception.HttpBadParameterException
import codetest.bookstore.api.exception.HttpConflictException
import codetest.bookstore.api.exception.HttpNotFoundException
import codetest.bookstore.api.generated.model.Error
import codetest.bookstore.exception.ConflictException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

/**
 * 例外ハンドラークラス
 */
@RestControllerAdvice
class ExceptionHandler {
    /**
     * HTTP400
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpBadParameterException::class)
    fun handleHttpBadParameterException(exception: RuntimeException): Error {
        return buildError(exception)
    }

    /**
     * HTTP404
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(HttpNotFoundException::class)
    fun handleHttpNotFoundException(exception: RuntimeException): Error {
        return buildError(exception)
    }

    /**
     * HTTP409
     */
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(HttpConflictException::class)
    fun handleHttpConflictException(exception: RuntimeException): Error {
        return buildError(exception)
    }

    /**
     * HTTP500
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception::class)
    fun handleException(exception: Exception): Error {
        return Error(message = exception.message)
    }

    private fun buildError(exception: RuntimeException): Error {
        return Error(message = exception.message)
    }
}