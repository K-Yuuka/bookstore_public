package codetest.bookstore.api.controller.impl

import codetest.bookstore.api.generated.controller.BookApi
import codetest.bookstore.api.generated.model.Book
import codetest.bookstore.api.generated.model.BookNameRequest
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
class BookApiImpl(
    private val bookStoreService: BookStoreService
) : BookApi {
    /**
     * 書籍を検索します
     */
    override fun bookGet(bookName: String?): ResponseEntity<List<Book>> =
        ResponseEntity.ok(toApiBookListModel(bookStoreService.getBookListByName(bookName).getOrThrow()))

    /**
     * 書籍を編集します
     */
    override fun bookBookIdPut(bookId: Int, bookNameRequest: BookNameRequest): ResponseEntity<Unit> {
        require(!bookNameRequest.bookName.isNullOrBlank()) { "book_name is required." }
        bookStoreService.editBookName(bookId, bookNameRequest.bookName).getOrThrow()
        return ResponseEntity.noContent().build()
    }

    /**
     * 書籍を削除します
     */
    override fun bookBookIdDelete(bookId: Int): ResponseEntity<Unit> {
        bookStoreService.deleteBook(bookId).getOrThrow()
        return ResponseEntity.noContent().build()
    }

    private fun toApiBookListModel(bookInfoList: List<BookInfo>): List<Book> =
        bookInfoList.map { toApiBookModel(it) }.toList()

    private fun toApiBookModel(bookInfo: BookInfo): Book = Book(id = bookInfo.bookId, name = bookInfo.bookName)
}