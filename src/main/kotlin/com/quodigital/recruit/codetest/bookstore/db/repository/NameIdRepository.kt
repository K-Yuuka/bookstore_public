package com.quodigital.recruit.codetest.bookstore.db.repository

interface NameIdRepository<TRecordData> {
    /*** [name]（部分一致）で検索して、結果リストを返す */
    fun getByName(name: String): List<TRecordData>

    /*** 登録されている書籍名と著者のリストを返す */
    fun getAll(): List<TRecordData>

    /*** [name]を登録して、登録情報を返す */
    fun add(name: String): TRecordData?

    /*** [id]の書籍の名前を[name]に変更する */
    fun edit(id: Int, name: String)

    /*** [id]の書籍を削除する */
    fun delete(id: Int)

    /*** [id]の書籍が存在する場合はtrueを返す */
    fun exists(id: Int): Boolean
}