package com.quodigital.recruit.codetest.bookstore.service

import com.quodigital.recruit.codetest.bookstore.exception.ConflictException
import com.quodigital.recruit.codetest.bookstore.exception.NotFoundException
import com.quodigital.recruit.codetest.bookstore.model.AuthorAndBookList
import com.quodigital.recruit.codetest.bookstore.model.AuthorInfo
import com.quodigital.recruit.codetest.bookstore.model.BookInfo
import org.springframework.stereotype.Service

/**
 * 書籍サービスクラス
 */
@Service
interface BookStoreService {
    /**
     * 書籍と著者名のリストを取得する
     *
     * @return 取得結果
     */
    fun getAuthorAndBookList(): List<AuthorAndBookList>

    /**
     *  書籍と著者を追加する（組み合わせの重複禁止）
     *
     * @param bookName 書籍名
     * @param authorName 著者
     * @return 追加した書籍と著者の情報
     */
    @Throws(ConflictException::class)
    fun addBookAndAuthor(bookName: String, authorName: String): Pair<BookInfo, AuthorInfo>

    /**
     * 名前で書籍を検索する
     *
     * @param bookName 書籍名
     * @return 検索結果リスト
     */
    fun findBookByName(bookName: String): List<Pair<BookInfo, AuthorInfo>>

    /**
     *  著者名で書籍を検索する
     *
     * @param authorName 著者
     * @return 検索結果リスト
     * @throws NotFoundException 検索結果の件数が0件の場合
     */
    @Throws(NotFoundException::class)
    fun findBookByAuthorName(authorName: String): List<AuthorAndBookList>

    /**
     * 名前で著者を検索する
     *
     * @param authorName 著者名
     * @return 検索結果リスト
     */
    fun findAuthorByName(authorName: String): List<AuthorInfo>

    /**
     * 書籍を編集する
     *
     * @param bookId 編集対象の書籍ID
     * @param bookName 書籍名
     */
    @Throws(ConflictException::class)
    fun editBook(bookId: Int, bookName: String)

    /**
     * 著者を編集する
     *
     * @param authorId 編集対象の著者ID
     * @param authorName 編集後の著者名
     */
    fun editAuthor(authorId: Int, authorName: String)

    /**
     * 著者を削除する
     * @param authorId 著者ID
     */
    @Throws(ConflictException::class)
    fun deleteAuthor(authorId: Int)

    /**
     * 書籍を削除する
     *
     * @param bookId 書籍ID
     */
    fun deleteBook(bookId: Int)
}