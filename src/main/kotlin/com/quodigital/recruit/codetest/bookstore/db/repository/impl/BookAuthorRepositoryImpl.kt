package com.quodigital.recruit.codetest.bookstore.db.repository.impl

import com.quodigital.recruit.codetest.bookstore.db.generated.tables.pojos.JAuthor
import com.quodigital.recruit.codetest.bookstore.db.generated.tables.pojos.JBook
import com.quodigital.recruit.codetest.bookstore.db.generated.tables.pojos.JBookAuthor
import com.quodigital.recruit.codetest.bookstore.db.generated.tables.references.AUTHOR
import com.quodigital.recruit.codetest.bookstore.db.generated.tables.references.BOOK
import com.quodigital.recruit.codetest.bookstore.db.generated.tables.references.BOOK_AUTHOR
import com.quodigital.recruit.codetest.bookstore.db.repository.BookAuthorRepository
import org.jooq.DSLContext
import org.springframework.stereotype.Repository

/**
 * 書籍&著者レポジトリクラス
 */
@Repository
class BookAuthorRepositoryImpl(
    private val dslContext: DSLContext
) : BookAuthorRepository {
    override fun getAll(): List<Pair<JAuthor, JBook>> {
        return dslContext.select()
            .from(BOOK_AUTHOR)
            .leftJoin(BOOK)
            .on(BOOK.BOOK_ID.eq(BOOK_AUTHOR.BOOK_ID))
            .leftJoin(AUTHOR)
            .on(AUTHOR.AUTHOR_ID.eq(BOOK_AUTHOR.AUTHOR_ID))
            .fetch()
            .map {
                Pair(it.into(JAuthor::class.java), it.into(JBook::class.java))
            }.toList()
    }

    override fun getByAuthorName(authorName: String): List<Pair<JAuthor, JBook>> {
        return dslContext.select()
            .from(BOOK_AUTHOR)
            .leftJoin(BOOK)
            .on(BOOK.BOOK_ID.eq(BOOK_AUTHOR.BOOK_ID))
            .leftJoin(AUTHOR)
            .on(AUTHOR.AUTHOR_ID.eq(BOOK_AUTHOR.AUTHOR_ID))
            .where(AUTHOR.AUTHOR_NAME.like("%${authorName}%"))
            .fetch()
            .map {
                Pair(it.into(JAuthor::class.java), it.into(JBook::class.java))
            }.toList()
    }

    override fun exists(bookName: String, authorName: String): Boolean {
        return dslContext.fetchExists(
            dslContext.selectOne()
                .from(BOOK_AUTHOR)
                .leftJoin(BOOK)
                .on(BOOK.BOOK_ID.eq(BOOK_AUTHOR.BOOK_ID))
                .leftJoin(AUTHOR)
                .on(AUTHOR.AUTHOR_ID.eq(BOOK_AUTHOR.AUTHOR_ID))
                .where(BOOK.BOOK_NAME.eq(bookName))
                .and(AUTHOR.AUTHOR_NAME.eq(authorName))
        )
    }

    override fun add(bookId: Int, authorId: Int): JBookAuthor? {
        return dslContext
            .insertInto(BOOK_AUTHOR, BOOK_AUTHOR.BOOK_ID, BOOK_AUTHOR.AUTHOR_ID)
            .values(bookId, authorId)
            .onConflictDoNothing()
            .returningResult(BOOK_AUTHOR.BOOK_ID, BOOK_AUTHOR.AUTHOR_ID)
            .fetchOne()
            ?.into(JBookAuthor::class.java)
    }
}