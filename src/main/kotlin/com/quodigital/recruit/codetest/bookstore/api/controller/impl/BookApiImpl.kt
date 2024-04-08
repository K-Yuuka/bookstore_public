package com.quodigital.recruit.codetest.bookstore.api.controller.impl

import com.quodigital.recruit.codetest.bookstore.api.generated.controller.BookApi
import com.quodigital.recruit.codetest.bookstore.api.generated.model.Author
import com.quodigital.recruit.codetest.bookstore.api.generated.model.Book
import com.quodigital.recruit.codetest.bookstore.api.generated.model.BookAuthorSet
import com.quodigital.recruit.codetest.bookstore.api.generated.model.BookNameRequest
import com.quodigital.recruit.codetest.bookstore.model.AuthorInfo
import com.quodigital.recruit.codetest.bookstore.model.BookInfo
import com.quodigital.recruit.codetest.bookstore.service.BookStoreService
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
    override fun bookBookNameGet(bookName: String): ResponseEntity<List<BookAuthorSet>> =
        ResponseEntity.ok(toApiBookAuthorListModel(bookStoreService.findBookByName(bookName)))

    /**
     * 書籍を編集します
     */
    override fun bookBookIdPut(bookId: Int, bookNameRequest: BookNameRequest): ResponseEntity<Unit> {
        if(bookNameRequest.bookName == null) {
            throw IllegalArgumentException("book_name is required.")
        } else {
            bookStoreService.editBook(bookId, bookNameRequest.bookName)
            return ResponseEntity.noContent().build()
        }
    }

    /**
     * 書籍を削除します
     */
    override fun bookBookIdDelete(bookId: Int): ResponseEntity<Unit> {
        bookStoreService.deleteBook(bookId)
        return ResponseEntity.noContent().build()
    }

    private fun toApiBookListModel(bookList: List<BookInfo>): List<Book> =
        bookList.stream().map { toApiBookModel(it) }.toList()

    private fun toApiBookModel(book: BookInfo): Book = Book(book.bookId, book.bookName)

    private fun toApiBookAuthorListModel(bookAuthorList: List<Pair<BookInfo, AuthorInfo>>): List<BookAuthorSet> {
        return bookAuthorList
            .stream()
            .map {
                toApiBookAuthorModel(it)
            }
            .toList()
    }

    private fun toApiBookAuthorModel(bookAuthorList: Pair<BookInfo, AuthorInfo>): BookAuthorSet {
        return BookAuthorSet(
            Book(bookAuthorList.first.bookId, bookAuthorList.first.bookName),
            Author(bookAuthorList.second.authorId, bookAuthorList.second.authorName)
        )
    }
}