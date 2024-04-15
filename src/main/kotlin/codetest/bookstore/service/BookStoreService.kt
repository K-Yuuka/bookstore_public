package codetest.bookstore.service

import codetest.bookstore.model.AuthorAndRelationalBooks
import codetest.bookstore.model.AuthorInfo
import codetest.bookstore.model.BookAuthorInfo
import codetest.bookstore.model.BookInfo
import org.springframework.stereotype.Service

/**
 * 書籍サービスクラス
 */
@Service
interface BookStoreService {
    /**
     *  書籍と著者を追加する（組み合わせの重複禁止）
     */
    fun addBookAndAuthor(bookName: String, authorName: String): BookAuthorInfo

    /**
     * [bookName]で書籍を検索する。nullの場合は登録されている書籍すべてを取得する
     */
    fun getBookListByName(bookName: String?): Result<List<BookInfo>>

    /**
     *  [authorName]で書籍を検索する。nullの場合は登録されている書籍&著者すべてを取得する
     */
    fun getBookListByAuthorName(authorName: String?): Result<List<AuthorAndRelationalBooks>>

    /**
     * [authorName]で著者を検索する。nullの場合は登録されている書籍すべてを取得する
     */
    fun getAuthorListByName(authorName: String?): Result<List<AuthorInfo>>

    /**
     * [bookId]で指定された書籍の名前を[bookName]に変更する
     */
    fun editBookName(bookId: Int, bookName: String) : Result<Unit>

    /**
     * [authorId]で指定された著者の名前を[authorName]に変更する
     */
    fun editAuthorName(authorId: Int, authorName: String) : Result<Unit>

    /**
     * [bookId]で指定された書籍の著者を[authorName]に変更する
     */
    fun editAuthor(bookId: Int, authorName: String): Result<BookAuthorInfo>

    /**
     * [bookId]で指定された書籍を削除する
     */
    fun deleteBook(bookId: Int) : Result<Unit>

    /**
     * [authorId]で指定された著者を削除する
     */
    fun deleteAuthor(authorId: Int) : Result<Unit>
}