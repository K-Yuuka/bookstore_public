package com.quodigital.recruit.codetest.bookstore.db.repository.impl

import com.quodigital.recruit.codetest.bookstore.db.generated.tables.pojos.JAuthor
import com.quodigital.recruit.codetest.bookstore.db.generated.tables.pojos.JBook
import com.quodigital.recruit.codetest.bookstore.db.generated.tables.pojos.JBookAuthor
import com.quodigital.recruit.codetest.bookstore.db.generated.tables.references.AUTHOR
import com.quodigital.recruit.codetest.bookstore.db.generated.tables.references.BOOK
import com.quodigital.recruit.codetest.bookstore.db.generated.tables.references.BOOK_AUTHOR
import com.quodigital.recruit.codetest.bookstore.db.repository.BookAuthorRepository
import org.jooq.Condition
import org.jooq.DSLContext
import org.springframework.stereotype.Repository

/**
 * 書籍&著者レポジトリクラス
 */
@Repository
class BookAuthorRepositoryImpl(
    private val dslContext: DSLContext
) : BookAuthorRepository {
    override fun getAll(): Map<JAuthor, List<JBook>> = get(null)

    override fun getByAuthorName(authorName: String): Map<JAuthor, List<JBook>> =
        getByAuthorName(authorName, false)

    override fun getByAuthorName(authorName: String, exactMatch: Boolean): Map<JAuthor, List<JBook>> {
        return if (exactMatch) {
            get(AUTHOR.AUTHOR_NAME.eq(authorName))
        } else {
            get(AUTHOR.AUTHOR_NAME.like("%${authorName}%"))
        }
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

    override fun editAuthor(bookId: Int, authorId: Int) {
        dslContext
            .update(BOOK_AUTHOR)
            .set(BOOK_AUTHOR.AUTHOR_ID, authorId)
            .where(BOOK_AUTHOR.BOOK_ID.eq(bookId))
            .execute()
    }

    private fun get(condition: Condition?): Map<JAuthor, List<JBook>> {
        val query = dslContext.select()
            .from(BOOK_AUTHOR)
            .leftJoin(BOOK)
            .on(BOOK.BOOK_ID.eq(BOOK_AUTHOR.BOOK_ID))
            .leftJoin(AUTHOR)
            .on(AUTHOR.AUTHOR_ID.eq(BOOK_AUTHOR.AUTHOR_ID))
        condition?.let { query.where(condition) }
        return query
            .fetchGroups(AUTHOR, BOOK)
            .mapKeys {
                it.key.into(JAuthor::class.java)
            }
            .mapValues {
                it.value.into(JBook::class.java)
            }
    }
}