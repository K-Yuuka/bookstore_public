package com.quodigital.recruit.codetest.bookstore.api.controller.impl

import com.quodigital.recruit.codetest.bookstore.api.generated.controller.BookstoreApi
import com.quodigital.recruit.codetest.bookstore.api.generated.model.*
import com.quodigital.recruit.codetest.bookstore.model.AuthorAndBookList
import com.quodigital.recruit.codetest.bookstore.model.AuthorInfo
import com.quodigital.recruit.codetest.bookstore.model.BookInfo
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
        bookAndAuthorRequest.bookName ?: let { throw IllegalArgumentException("book_name is required.") }
        bookAndAuthorRequest.authorName ?: let { throw IllegalArgumentException("author_name is required.") }
        return ResponseEntity.created(URI("")).body(
            toApiBookAuthorSetModel(
                bookService.addBookAndAuthor(bookAndAuthorRequest.bookName, bookAndAuthorRequest.authorName)
            )
        )
    }

    /**
     * 書籍と著者者のリストを取得する
     */
    override fun bookstoreListGet(): ResponseEntity<List<AuthorAndBookListInner>> =
        ResponseEntity.ok(toApiAuthorAndBookListInnerModelList(bookService.getAuthorAndBookList()))

    /**
     * 著者を指定して書籍を検索する
     */
    override fun bookstoreAuthorNameGet(authorName: String): ResponseEntity<List<AuthorAndBookListInner>> =
        ResponseEntity.ok(toApiAuthorAndBookListInnerModelList(bookService.findBookByAuthorName(authorName)))

    private fun toApiBookAuthorSetModel(bookAuthor: Pair<BookInfo, AuthorInfo>): BookAuthorSet {
        return BookAuthorSet(
            Book(bookAuthor.first.bookId, bookAuthor.first.bookName),
            Author(bookAuthor.second.authorId, bookAuthor.second.authorName)
        )
    }

    private fun toApiAuthorAndBookListInnerModelList(authorBookSetList: List<AuthorAndBookList>)
            : List<AuthorAndBookListInner> =
        authorBookSetList.map { toApiAuthorAndBookListInnerModel(it) }.toList()

    private fun toApiAuthorAndBookListInnerModel(authorBookSet: AuthorAndBookList): AuthorAndBookListInner {
        return AuthorAndBookListInner(
            Author(
                authorBookSet.author.authorId, authorBookSet.author.authorName
            ),
            authorBookSet.bookList.map {
                Book(it.bookId, it.bookName)
            }.toList()
        )
    }
}