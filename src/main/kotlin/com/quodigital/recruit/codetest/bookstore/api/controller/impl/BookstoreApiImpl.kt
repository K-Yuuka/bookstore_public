package com.quodigital.recruit.codetest.bookstore.api.controller.impl

import com.quodigital.recruit.codetest.bookstore.api.generated.controller.BookstoreApi
import com.quodigital.recruit.codetest.bookstore.api.generated.model.*
import com.quodigital.recruit.codetest.bookstore.model.AuthorAndRelationalBooks
import com.quodigital.recruit.codetest.bookstore.model.BookAuthorInfo
import com.quodigital.recruit.codetest.bookstore.service.BookStoreService
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import java.net.URI

/**
 * 書籍&著者のAPI
 */
@CrossOrigin
@Controller
class BookstoreApiImpl(
    private val bookService: BookStoreService
) : BookstoreApi {
    /**
     * 書籍と著者を追加する
     */
    override fun bookstorePost(bookAndAuthorRequest: BookAndAuthorRequest): ResponseEntity<BookAuthorSet> {
        require(!bookAndAuthorRequest.bookName.isNullOrBlank()) { "book_name is required." }
        require(!bookAndAuthorRequest.authorName.isNullOrBlank()) { "author_name is required." }
        return ResponseEntity.created(URI("")).body(
            toApiBookAuthorSetModel(
                bookService.addBookAndAuthor(bookAndAuthorRequest.bookName, bookAndAuthorRequest.authorName)
                    .getOrThrow()
            )
        )
    }

    /**
     * 書籍の著者を変更する
     */
    override fun bookstoreBookIdPut(bookId: Int, authorRequest: AuthorRequest): ResponseEntity<BookAuthorSet> {
        require(!authorRequest.authorName.isNullOrBlank()) { "author_name is required." }
        return ResponseEntity.ok(
            toApiBookAuthorSetModel(
                bookService.editAuthor(bookId, authorRequest.authorName).getOrThrow()
            )
        )
    }

    /**
     * 著者&書籍を取得する
     */
    override fun bookstoreGet(authorName: String?): ResponseEntity<List<AuthorAndBookListInner>> = ResponseEntity.ok(
        toApiAuthorAndBookListInnerModelList(
            bookService.getBookListByAuthorName(authorName).getOrThrow()
        )
    )

    private fun toApiBookAuthorSetModel(bookAuthor: BookAuthorInfo): BookAuthorSet =
        BookAuthorSet(
            Book(bookAuthor.bookInfo.bookId, bookAuthor.bookInfo.bookName),
            Author(bookAuthor.authorInfo.authorId, bookAuthor.authorInfo.authorName)
        )

    private fun toApiAuthorAndBookListInnerModelList(authorBookSetList: List<AuthorAndRelationalBooks>)
            : List<AuthorAndBookListInner> =
        authorBookSetList.map { toApiAuthorAndBookListInnerModel(it) }.toList()

    private fun toApiAuthorAndBookListInnerModel(authorBookSet: AuthorAndRelationalBooks): AuthorAndBookListInner =
        AuthorAndBookListInner(
            Author(authorBookSet.author.authorId, authorBookSet.author.authorName),
            authorBookSet.bookList.map { Book(it.bookId, it.bookName) }.toList()
        )
}