package codetest.bookstore.api.controller.impl

import codetest.bookstore.api.exception.HttpBadParameterException
import codetest.bookstore.api.exception.HttpConflictException
import codetest.bookstore.api.exception.HttpNotFoundException
import codetest.bookstore.api.generated.controller.BookstoreApi
import codetest.bookstore.api.generated.model.*
import codetest.bookstore.exception.ConflictException
import codetest.bookstore.exception.NotFoundException
import codetest.bookstore.model.AuthorAndRelationalBooks
import codetest.bookstore.model.BookAuthorInfo
import codetest.bookstore.service.BookStoreService
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import java.net.URI

/**
 * 書籍&著者のAPI
 */
@CrossOrigin
@Controller
class BookstoreController(
    private val bookService: BookStoreService
) : BookstoreApi {
    /**
     * 書籍と著者を追加する
     */
    override fun bookstorePost(bookAndAuthorRequest: BookAndAuthorRequest): ResponseEntity<BookAuthorSet> {
        try {
            require(!bookAndAuthorRequest.bookName.isNullOrBlank()) { "book_name is required." }
            require(!bookAndAuthorRequest.authorName.isNullOrBlank()) { "author_name is required." }
            return ResponseEntity
                .created(URI(""))
                .body(
                    toApiBookAuthorSetModel(
                        bookService.addBookAndAuthor(bookAndAuthorRequest.bookName, bookAndAuthorRequest.authorName)
                    )
                )
        } catch (e: IllegalArgumentException) {
            throw HttpBadParameterException(e)
        } catch (e: ConflictException) {
            throw HttpConflictException(e)
        }
    }

    /**
     * 書籍の著者を変更する
     */
    override fun bookstoreBookIdPut(bookId: Int, authorNameRequest: AuthorNameRequest): ResponseEntity<BookAuthorSet> {
        try {
            require(!authorNameRequest.authorName.isNullOrBlank()) { "author_name is required." }
            return ResponseEntity.ok(
                toApiBookAuthorSetModel(
                    bookService.editAuthor(bookId, authorNameRequest.authorName)
                )
            )
        } catch (e: IllegalArgumentException) {
            throw HttpBadParameterException(e)
        } catch (e: NotFoundException) {
            throw HttpNotFoundException(e)
        } catch (e: ConflictException) {
            throw HttpConflictException(e)
        }
    }

    /**
     * 著者&書籍を取得する
     */
    override fun bookstoreGet(authorName: String?): ResponseEntity<List<AuthorAndBookListInner>> {
        try {
            return ResponseEntity.ok(
                toApiAuthorAndBookListInnerModelList(
                    bookService.getBookListByAuthorName(authorName)
                )
            )
        } catch (e: NotFoundException) {
            throw HttpNotFoundException(e)
        }
    }

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