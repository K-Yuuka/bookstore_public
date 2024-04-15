package codetest.bookstore.api.controller.impl

import codetest.bookstore.api.generated.controller.AuthorApi
import codetest.bookstore.api.generated.model.Author
import codetest.bookstore.api.generated.model.AuthorNameRequest
import codetest.bookstore.model.AuthorInfo
import codetest.bookstore.service.BookStoreService
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
    override fun authorGet(authorName: String?): ResponseEntity<List<Author>> =
        ResponseEntity.ok(toApiAuthorListModel(bookStoreService.getAuthorListByName(authorName).getOrThrow()))

    /**
     * 著者名を編集します
     */
    override fun authorAuthorIdPut(authorId: Int, authorNameRequest: AuthorNameRequest): ResponseEntity<Unit> {
        require(!authorNameRequest.authorName.isNullOrBlank()) { "author_name is required." }
        bookStoreService.editAuthorName(authorId, authorNameRequest.authorName).getOrThrow()
        return ResponseEntity.noContent().build()
    }

    /**
     * 著者を削除します
     */
    override fun authorAuthorIdDelete(authorId: Int): ResponseEntity<Unit> {
        bookStoreService.deleteAuthor(authorId).getOrThrow()
        return ResponseEntity.noContent().build()
    }

    private fun toApiAuthorListModel(recordList: List<AuthorInfo>): List<Author> =
        recordList.stream().map { toApiAuthorModel(it) }.toList()

    private fun toApiAuthorModel(record: AuthorInfo): Author = Author(record.authorId, record.authorName)
}