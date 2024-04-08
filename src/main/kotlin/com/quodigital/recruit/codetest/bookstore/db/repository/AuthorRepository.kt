package com.quodigital.recruit.codetest.bookstore.db.repository

import com.quodigital.recruit.codetest.bookstore.db.generated.tables.pojos.JAuthor

/**
 * 著者レポジトリ
 */
interface AuthorRepository {
    /**
     * 著者をすべて取得する
     *
     * @return 結果リスト
     */
    fun getAll(): List<JAuthor>

    /**
     * 著者を名前で検索する
     *
     * @param authorName 著者名
     * @return 検索結果リスト
     */
    fun getByName(authorName: String): List<JAuthor>

    /**
     * 著者情報を追加する
     * @param authorName 著者名
     * @return 登録結果
     */
    fun add(authorName: String): JAuthor?

    /**
     * 著者情報の追加する
     * すでに存在する場合はその情報を返す
     * @param authorName 著者名
     * @return 登録情報 または 登録済み情報
     */
    fun addOrGetExistsInfo(authorName: String): JAuthor

    /**
     * 著者を編集する
     */
    fun edit(authorId: Int, authorName: String)

    /**
     * 著者を削除する
     */
    fun delete(authorId: Int)
}