package com.quodigital.recruit.codetest.bookstore.api.controller.impl

import com.quodigital.recruit.codetest.bookstore.api.generated.controller.AuthorApi
import com.quodigital.recruit.codetest.bookstore.api.generated.model.Author
import com.quodigital.recruit.codetest.bookstore.api.generated.model.AuthorRequest
import com.quodigital.recruit.codetest.bookstore.model.AuthorInfo
import com.quodigital.recruit.codetest.bookstore.service.BookStoreService
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin

/**
 * 著者APIクラス
 */
@CrossOrigin
@Controller
class AuthorApiImpl(
    private val bookStoreService: BookStoreService
) : AuthorApi {
    /**
     * 著者を検索します
     */
    override fun authorAuthorNameGet(authorName: String): ResponseEntity<List<Author>> =
        ResponseEntity.ok(toApiAuthorListModel(bookStoreService.findAuthorByName(authorName)))

    /**
     * 著者名を編集します
     */
    override fun authorAuthorIdPut(authorId: Int, authorRequest: AuthorRequest): ResponseEntity<Unit> {
        authorRequest.authorName ?: let { throw IllegalArgumentException("author_name is required.") }
        bookStoreService.editAuthor(authorId, authorRequest.authorName)
        return ResponseEntity.ok(null)
    }

    /**
     * 著者を削除します
     */
    override fun authorAuthorIdDelete(authorId: Int): ResponseEntity<Unit> {
        bookStoreService.deleteAuthor(authorId)
        return ResponseEntity.noContent().build()
    }

    private fun toApiAuthorListModel(recordList: List<AuthorInfo>): List<Author> =
        recordList.stream().map { toApiAuthorModel(it) }.toList()

    private fun toApiAuthorModel(record: AuthorInfo): Author = Author(record.authorId, record.authorName)
}