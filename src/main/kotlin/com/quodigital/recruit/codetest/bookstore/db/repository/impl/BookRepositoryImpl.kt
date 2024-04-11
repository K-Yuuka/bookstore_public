package com.quodigital.recruit.codetest.bookstore.db.repository.impl

import com.quodigital.recruit.codetest.bookstore.db.generated.tables.pojos.JBook
import com.quodigital.recruit.codetest.bookstore.db.generated.tables.references.BOOK
import com.quodigital.recruit.codetest.bookstore.db.repository.BookRepository
import org.jooq.Condition
import org.jooq.DSLContext
import org.springframework.stereotype.Repository

/**
 * 書籍レポジトリ実装クラス
 */
@Repository
class BookRepositoryImpl(
    private val dslContext: DSLContext
) : BookRepository {

    override fun getByName(name: String): List<JBook> {
        return get(BOOK.BOOK_NAME.like("%${name}%"))
    }

    override fun getAll(): List<JBook> {
        return get(null)
    }

    override fun add(name: String): JBook {
        return dslContext
            .insertInto(BOOK, BOOK.BOOK_NAME)
            .values(name)
            .returningResult(BOOK.BOOK_ID, BOOK.BOOK_NAME)
            .fetchOne()
            ?.into(JBook::class.java)!!
    }

    override fun edit(id: Int, name: String) {
        dslContext
            .update(BOOK)
            .set(BOOK.BOOK_NAME, name)
            .where(BOOK.BOOK_ID.eq(id))
            .execute()
    }

    override fun delete(id: Int) {
        dslContext
            .delete(BOOK)
            .where(BOOK.BOOK_ID.eq(id))
            .execute()
    }

    override fun exists(id: Int): Boolean = dslContext.fetchExists(BOOK, BOOK.BOOK_ID.eq(id))

    private fun get(condition: Condition?): List<JBook> {
        val query = dslContext.select().from(BOOK)
        condition?.let { query.where(condition) }
        return query
            .fetch()
            .map { it.into(JBook::class.java) }
            .toList()
    }
}