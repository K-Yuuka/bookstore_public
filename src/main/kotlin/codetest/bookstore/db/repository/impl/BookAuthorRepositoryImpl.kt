package codetest.bookstore.db.repository.impl

import codetest.bookstore.db.generated.tables.pojos.JAuthor
import codetest.bookstore.db.generated.tables.pojos.JBook
import codetest.bookstore.db.generated.tables.pojos.JBookAuthor
import codetest.bookstore.db.generated.tables.references.AUTHOR
import codetest.bookstore.db.generated.tables.references.BOOK
import codetest.bookstore.db.generated.tables.references.BOOK_AUTHOR
import codetest.bookstore.db.repository.BookAuthorRepository
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
    override fun getAll(): List<Pair<JAuthor, List<JBook>>> = get(null)

    override fun getByAuthorName(authorName: String): List<Pair<JAuthor, List<JBook>>> =
        get(AUTHOR.AUTHOR_NAME.like("%${escapeForLikePredicate(authorName)}%", '!'))

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
            .returningResult(BOOK_AUTHOR.BOOK_ID, BOOK_AUTHOR.AUTHOR_ID)
            .fetchOne()
            ?.into(JBookAuthor::class.java)
    }

    override fun editAuthor(bookId: Int, authorId: Int): Boolean {
        return dslContext
            .update(BOOK_AUTHOR)
            .set(BOOK_AUTHOR.AUTHOR_ID, authorId)
            .where(BOOK_AUTHOR.BOOK_ID.eq(bookId))
            .execute() == 1
    }

    private fun get(condition: Condition?): List<Pair<JAuthor, List<JBook>>> {
        val query = dslContext.select()
            .from(BOOK_AUTHOR)
            .leftJoin(BOOK)
            .on(BOOK.BOOK_ID.eq(BOOK_AUTHOR.BOOK_ID))
            .leftJoin(AUTHOR)
            .on(AUTHOR.AUTHOR_ID.eq(BOOK_AUTHOR.AUTHOR_ID))
        condition?.let { query.where(condition) }
        return query
            .fetchGroups(AUTHOR, BOOK)
            .map {
                Pair(it.key.into(JAuthor::class.java), it.value.into(JBook::class.java))
            }
    }
}