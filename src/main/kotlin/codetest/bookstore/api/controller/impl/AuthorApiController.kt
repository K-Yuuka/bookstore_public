package codetest.bookstore.api.controller.impl

import codetest.bookstore.api.exception.HttpBadParameterException
import codetest.bookstore.api.exception.HttpConflictException
import codetest.bookstore.api.exception.HttpNotFoundException
import codetest.bookstore.api.generated.controller.AuthorApi
import codetest.bookstore.api.generated.model.Author
import codetest.bookstore.api.generated.model.AuthorNameRequest
import codetest.bookstore.exception.ConflictException
import codetest.bookstore.exception.NotFoundException
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
class AuthorApiController(
    private val bookStoreService: BookStoreService
) : AuthorApi {
    /**
     * 著者を検索します
     */
    override fun authorGet(authorName: String?): ResponseEntity<List<Author>> {
        try {
            return ResponseEntity.ok(toApiAuthorListModel(bookStoreService.getAuthorListByName(authorName)))
        } catch (e: NotFoundException) {
            throw HttpNotFoundException(e)
        }
    }

    /**
     * 著者名を編集します
     */
    override fun authorAuthorIdPut(authorId: Int, authorNameRequest: AuthorNameRequest): ResponseEntity<Unit> {
        try {
            require(!authorNameRequest.authorName.isNullOrBlank()) { "author_name is required." }
            bookStoreService.editAuthorName(authorId, authorNameRequest.authorName)
            return ResponseEntity.noContent().build()
        } catch (e: IllegalArgumentException) {
            throw HttpBadParameterException(e)
        } catch (e: NotFoundException) {
            throw HttpNotFoundException(e)
        } catch (e: ConflictException) {
            throw HttpConflictException(e)
        }
    }

    /**
     * 著者を削除します
     */
    override fun authorAuthorIdDelete(authorId: Int): ResponseEntity<Unit> {
        try {
            bookStoreService.deleteAuthor(authorId)
            return ResponseEntity.noContent().build()
        } catch (e: NotFoundException) {
            throw HttpNotFoundException(e)
        } catch (e: ConflictException) {
            throw HttpConflictException(e)
        }
    }

    private fun toApiAuthorListModel(recordList: List<AuthorInfo>): List<Author> =
        recordList.stream().map { toApiAuthorModel(it) }.toList()

    private fun toApiAuthorModel(record: AuthorInfo): Author = Author(record.authorId, record.authorName)
}