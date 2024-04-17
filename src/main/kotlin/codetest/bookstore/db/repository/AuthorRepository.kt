package codetest.bookstore.db.repository

import codetest.bookstore.db.generated.tables.pojos.JAuthor

/**
 * 著者レポジトリ
 */
interface AuthorRepository : NameIdRepository<JAuthor> {
    /**
     * [authorName]の著者が未登録の場合には登録して登録情報を返し、登録済みの場合には登録済み情報を取得する
     */
    fun addOrGetExistsInfo(authorName: String): JAuthor?
}