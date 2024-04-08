package com.quodigital.recruit.codetest.bookstore.db.repository.impl

import com.quodigital.recruit.codetest.bookstore.db.generated.tables.pojos.JAuthor
import com.quodigital.recruit.codetest.bookstore.db.generated.tables.pojos.JBook
import com.quodigital.recruit.codetest.bookstore.db.generated.tables.references.BOOK
import com.quodigital.recruit.codetest.bookstore.db.repository.BookRepository
import org.jooq.DSLContext
import org.springframework.stereotype.Repository

/**
 * 書籍レポジトリ実装クラス
 */
@Repository
class BookRepositoryImpl(
    private val dslContext: DSLContext
) : BookRepository {
    override fun getAll(): List<JBook> {
        return dslContext.select()
            .from(BOOK)
            .fetch {
                it.into(JBook::class.java)
            }
            .toList()
    }

    override fun getByName(bookName: String): List<Pair<JBook, JAuthor>> {
        return dslContext.select()
            .from(BOOK)
            .where(BOOK.BOOK_NAME.like("%${bookName}%"))
            .fetch()
            .map {
                Pair(it.into(JBook::class.java), it.into(JAuthor::class.java))
            }
            .toList()
    }

    override fun add(bookName: String): JBook {
        return dslContext
            .insertInto(BOOK, BOOK.BOOK_NAME)
            .values(bookName)
            .returningResult(BOOK.BOOK_ID, BOOK.BOOK_NAME)
            .fetchOne()
            ?.into(JBook::class.java)!!
    }

    override fun edit(bookId: Int, bookName: String) {
        dslContext
            .update(BOOK)
            .set(BOOK.BOOK_NAME, bookName)
            .where(BOOK.BOOK_ID.eq(bookId))
            .execute()
    }

    override fun delete(bookId: Int) {
        dslContext
            .delete(BOOK)
            .where(BOOK.BOOK_ID.eq(bookId))
            .execute()
    }

}