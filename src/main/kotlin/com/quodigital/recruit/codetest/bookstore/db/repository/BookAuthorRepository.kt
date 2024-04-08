package com.quodigital.recruit.codetest.bookstore.db.repository

import com.quodigital.recruit.codetest.bookstore.db.generated.tables.pojos.JAuthor
import com.quodigital.recruit.codetest.bookstore.db.generated.tables.pojos.JBook
import com.quodigital.recruit.codetest.bookstore.db.generated.tables.pojos.JBookAuthor

/**
 * 書籍&著者のレポジトリ
 */
interface BookAuthorRepository {
    /**
     * 全情報取得
     *
     * @return 取得結果リスト
     */
    fun getAll(): List<Pair<JAuthor, JBook>>

    /**
     * 著者名で書籍を取得する
     *
     * @param authorName 著者名
     * @return 取得結果リスト
     */
    fun getByAuthorName(authorName: String): List<Pair<JAuthor, JBook>>

    /**
     * 指定の書籍と著者の組み合わせが登録済みかどうか
     *
     * @param bookName 書籍名
     * @param authorName 著者名
     * @return 登録済みである場合はtrue
     */
    fun exists(bookName: String, authorName: String): Boolean

    /**
     * 書籍と著者の関連を追加
     * @param bookId 書籍ID
     * @param authorId 著者ID
     */
    fun add(bookId: Int, authorId: Int): JBookAuthor?
}