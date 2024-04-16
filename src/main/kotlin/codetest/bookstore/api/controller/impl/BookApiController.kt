package codetest.bookstore.api.controller.impl

import codetest.bookstore.api.exception.HttpBadParameterException
import codetest.bookstore.api.exception.HttpConflictException
import codetest.bookstore.api.exception.HttpNotFoundException
import codetest.bookstore.api.generated.controller.BookApi
import codetest.bookstore.api.generated.model.Book
import codetest.bookstore.api.generated.model.BookNameRequest
import codetest.bookstore.exception.ConflictException
import codetest.bookstore.exception.NotFoundException
import codetest.bookstore.model.BookInfo
import codetest.bookstore.service.BookStoreService
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin

/**
 * 書籍APIクラス
 */
@CrossOrigin
@Controller
class BookApiController(
    private val bookStoreService: BookStoreService
) : BookApi {
    /**
     * 書籍を検索します
     */
    override fun bookGet(bookName: String?): ResponseEntity<List<Book>> {
        try {
            return ResponseEntity.ok(toApiBookListModel(bookStoreService.getBookListByName(bookName)))
        } catch (e: NotFoundException) {
            throw HttpNotFoundException(e)
        }
    }

    /**
     * 書籍を編集します
     */
    override fun bookBookIdPut(bookId: Int, bookNameRequest: BookNameRequest): ResponseEntity<Unit> {
        try {
            require(!bookNameRequest.bookName.isNullOrBlank()) { "book_name is required." }
            bookStoreService.editBookName(bookId, bookNameRequest.bookName)
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
     * 書籍を削除します
     */
    override fun bookBookIdDelete(bookId: Int): ResponseEntity<Unit> {
        try {
            bookStoreService.deleteBook(bookId)
            return ResponseEntity.noContent().build()
        } catch (e: NotFoundException) {
            throw HttpNotFoundException(e)
        }
    }

    private fun toApiBookListModel(bookInfoList: List<BookInfo>): List<Book> =
        bookInfoList.map { toApiBookModel(it) }.toList()

    private fun toApiBookModel(bookInfo: BookInfo): Book = Book(id = bookInfo.bookId, name = bookInfo.bookName)
}