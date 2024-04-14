/**
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech) (7.4.0).
 * https://openapi-generator.tech
 * Do not edit the class manually.
*/
package com.quodigital.recruit.codetest.bookstore.api.generated.controller

import com.quodigital.recruit.codetest.bookstore.api.generated.model.AuthorAndBookListInner
import com.quodigital.recruit.codetest.bookstore.api.generated.model.AuthorRequest
import com.quodigital.recruit.codetest.bookstore.api.generated.model.BookAndAuthorRequest
import com.quodigital.recruit.codetest.bookstore.api.generated.model.BookAuthorSet
import com.quodigital.recruit.codetest.bookstore.api.generated.model.Error
import io.swagger.v3.oas.annotations.*
import io.swagger.v3.oas.annotations.enums.*
import io.swagger.v3.oas.annotations.media.*
import io.swagger.v3.oas.annotations.responses.*
import io.swagger.v3.oas.annotations.security.*
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity

import org.springframework.web.bind.annotation.*
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.beans.factory.annotation.Autowired


import kotlin.collections.List
import kotlin.collections.Map

@RequestMapping("\${api.base-path:}")
interface BookstoreApi {

    @Operation(
        tags = ["bookstore",],
        summary = "書籍の著者を変更",
        operationId = "bookstoreBookIdPut",
        description = """書籍の著者を変更します。指定された著者が未登録の場合は登録します。""",
        responses = [
            ApiResponse(responseCode = "200", description = "取得成功", content = [Content(schema = Schema(implementation = BookAuthorSet::class))]),
            ApiResponse(responseCode = "400", description = "Bad request", content = [Content(schema = Schema(implementation = Error::class))]),
            ApiResponse(responseCode = "404", description = "Not found", content = [Content(schema = Schema(implementation = Error::class))]),
            ApiResponse(responseCode = "409", description = "Conflict", content = [Content(schema = Schema(implementation = Error::class))]),
            ApiResponse(responseCode = "200", description = "An unexpected exception occurred", content = [Content(schema = Schema(implementation = Error::class))])
        ]
    )
    @RequestMapping(
            method = [RequestMethod.PUT],
            value = ["/bookstore/{bookId}"],
            produces = ["application/json"],
            consumes = ["application/json"]
    )
    fun bookstoreBookIdPut(@Parameter(description = "変更対象の書籍IDを指定します", required = true) @PathVariable("bookId") bookId: kotlin.Int,@Parameter(description = "", required = true) @RequestBody authorRequest: AuthorRequest): ResponseEntity<BookAuthorSet> {
        return ResponseEntity(HttpStatus.NOT_IMPLEMENTED)
    }

    @Operation(
        tags = ["bookstore",],
        summary = "著者&書籍リストの取得",
        operationId = "bookstoreGet",
        description = """登録されているすべての著者と書籍のリストを取得します""",
        responses = [
            ApiResponse(responseCode = "200", description = "取得成功", content = [Content(array = ArraySchema(schema = Schema(implementation = AuthorAndBookListInner::class)))]),
            ApiResponse(responseCode = "404", description = "Not found", content = [Content(schema = Schema(implementation = Error::class))]),
            ApiResponse(responseCode = "200", description = "An unexpected exception occurred", content = [Content(schema = Schema(implementation = Error::class))])
        ]
    )
    @RequestMapping(
            method = [RequestMethod.GET],
            value = ["/bookstore"],
            produces = ["application/json"]
    )
    fun bookstoreGet(@Parameter(description = "著者を指定します。指定がない場合は登録されている情報すべてを取得します。") @RequestParam(value = "authorName", required = false) authorName: kotlin.String?): ResponseEntity<List<AuthorAndBookListInner>> {
        return ResponseEntity(HttpStatus.NOT_IMPLEMENTED)
    }

    @Operation(
        tags = ["bookstore",],
        summary = "書籍と著者を登録",
        operationId = "bookstorePost",
        description = """書籍と著者を登録します。書籍名と著者名、両方の指定が必要です。""",
        responses = [
            ApiResponse(responseCode = "201", description = "登録に成功", content = [Content(schema = Schema(implementation = BookAuthorSet::class))]),
            ApiResponse(responseCode = "400", description = "Bad request", content = [Content(schema = Schema(implementation = Error::class))]),
            ApiResponse(responseCode = "409", description = "Conflict", content = [Content(schema = Schema(implementation = Error::class))]),
            ApiResponse(responseCode = "200", description = "An unexpected exception occurred", content = [Content(schema = Schema(implementation = Error::class))])
        ]
    )
    @RequestMapping(
            method = [RequestMethod.POST],
            value = ["/bookstore"],
            produces = ["application/json"],
            consumes = ["application/json"]
    )
    fun bookstorePost(@Parameter(description = "", required = true) @RequestBody bookAndAuthorRequest: BookAndAuthorRequest): ResponseEntity<BookAuthorSet> {
        return ResponseEntity(HttpStatus.NOT_IMPLEMENTED)
    }
}
