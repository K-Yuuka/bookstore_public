package com.quodigital.recruit.codetest.bookstore.db.repository.impl

import com.quodigital.recruit.codetest.bookstore.db.generated.tables.pojos.JAuthor
import com.quodigital.recruit.codetest.bookstore.db.generated.tables.references.AUTHOR
import com.quodigital.recruit.codetest.bookstore.db.repository.AuthorRepository
import org.jooq.Condition
import org.jooq.DSLContext
import org.springframework.stereotype.Repository

/**
 * 著者レポジトリ実装クラス
 */
@Repository
class AuthorRepositoryImpl(
    private val dslContext: DSLContext
) : AuthorRepository {
    override fun getByName(name: String): List<JAuthor> {
        return get(AUTHOR.AUTHOR_NAME.like("%${name}%"))
    }

    override fun getById(id: Int): JAuthor? {
        return dslContext
            .select()
            .from(AUTHOR)
            .where(AUTHOR.AUTHOR_ID.eq(id))
            .fetchOne()
            ?.into(JAuthor::class.java)
    }

    override fun getAll(): List<JAuthor> {
        return get(null)
    }

    override fun add(name: String): JAuthor {
        return dslContext
            .insertInto(AUTHOR, AUTHOR.AUTHOR_NAME)
            .values(name)
            .returningResult(AUTHOR.AUTHOR_ID, AUTHOR.AUTHOR_NAME)
            .fetchOne()
            ?.into(JAuthor::class.java)!!
    }

    override fun addOrGetExistsInfo(authorName: String): JAuthor {
        return get(authorName) ?: let {
            return dslContext
                .insertInto(AUTHOR, AUTHOR.AUTHOR_NAME)
                .values(authorName)
                .onConflict()
                .doNothing()
                .returningResult(AUTHOR.AUTHOR_ID, AUTHOR.AUTHOR_NAME)
                .fetchOne()
                ?.into(JAuthor::class.java)!!
        }
    }

    override fun edit(id: Int, name: String): Boolean {
        return dslContext
            .update(AUTHOR)
            .set(AUTHOR.AUTHOR_NAME, name)
            .where(AUTHOR.AUTHOR_ID.eq(id))
            .execute() == 1
    }

    override fun delete(id: Int): Boolean {
        return dslContext
            .delete(AUTHOR)
            .where(AUTHOR.AUTHOR_ID.eq(id))
            .execute() == 1
    }

    override fun exists(id: Int): Boolean = dslContext.fetchExists(AUTHOR, AUTHOR.AUTHOR_ID.eq(id))

    private fun get(authorName: String): JAuthor? {
        val resultList = get(AUTHOR.AUTHOR_NAME.eq(authorName))
        return if (resultList.isEmpty()) null else resultList[0]
    }

    private fun get(condition: Condition?): List<JAuthor> {
        val query = dslContext.select().from(AUTHOR)
        condition?.let { query.where(condition) }
        return query.fetch { it.into(JAuthor::class.java) }.toList()
    }
}