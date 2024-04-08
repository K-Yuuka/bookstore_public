package com.quodigital.recruit.codetest.bookstore.db.repository

import com.quodigital.recruit.codetest.bookstore.db.generated.tables.pojos.JAuthor
import com.quodigital.recruit.codetest.bookstore.db.generated.tables.pojos.JBook

/**
 * 書籍レポジトリ
 */
interface BookRepository {
    /**
     * 書籍をすべて取得する
     *
     * @return 結果リスト
     */
    fun getAll(): List<JBook>

    /**
     * 書籍を名前で検索する
     *
     * @param bookName 書籍名
     * @return 検索結果リスト
     */
    fun getByName(bookName: String): List<Pair<JBook, JAuthor>>

    /**
     * 書籍を追加する
     * @param bookName 書籍名
     * @return 登録結果
     */
    fun add(bookName: String): JBook

    /**
     * 書籍名を編集する
     *
     * @param bookId 書籍ID
     * @param bookName 編集後の書籍名
     */
    fun edit(bookId: Int, bookName: String)

    /**
     * 書籍を削除する
     */
    fun delete(bookId: Int)
}