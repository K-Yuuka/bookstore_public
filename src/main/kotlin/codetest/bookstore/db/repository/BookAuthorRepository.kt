package codetest.bookstore.db.repository

import codetest.bookstore.db.generated.tables.pojos.JAuthor
import codetest.bookstore.db.generated.tables.pojos.JBook
import codetest.bookstore.db.generated.tables.pojos.JBookAuthor

/**
 * 書籍&著者のレポジトリ
 */
interface BookAuthorRepository {
    /**
     * 登録されている書籍と著者の情報をすべて取得する
     */
    fun getAll(): List<Pair<JAuthor, List<JBook>>>

    /**
     * [authorName]（部分一致）で書籍を検索して、著者と紐づく書籍リストを返す
     */
    fun getByAuthorName(authorName: String): List<Pair<JAuthor, List<JBook>>>

    /**
     * [bookName]と[authorName]の組み合わせが存在する場合はtrueを返す
     */
    fun exists(bookName: String, authorName: String): Boolean

    /**
     * [bookId]と[authorId]の関連を登録して、登録情報を返す
     */
    fun add(bookId: Int, authorId: Int): JBookAuthor?

    /**
     * [bookId]で指定された書籍の著者を[authorId]に変更する
     */
    fun editAuthor(bookId: Int, authorId: Int): Boolean
}