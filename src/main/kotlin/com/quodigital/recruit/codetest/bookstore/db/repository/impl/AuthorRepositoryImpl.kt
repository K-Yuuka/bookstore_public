package com.quodigital.recruit.codetest.bookstore.db.repository.impl

import com.quodigital.recruit.codetest.bookstore.db.generated.tables.pojos.JAuthor
import com.quodigital.recruit.codetest.bookstore.db.generated.tables.references.AUTHOR
import com.quodigital.recruit.codetest.bookstore.db.repository.AuthorRepository
import org.jooq.DSLContext
import org.springframework.stereotype.Repository

/**
 * 著者レポジトリ実装クラス
 */
@Repository
class AuthorRepositoryImpl(
    private val dslContext: DSLContext
) : AuthorRepository {
    override fun getAll(): List<JAuthor> {
        return dslContext.select()
            .from(AUTHOR)
            .fetch {
                it.into(JAuthor::class.java)
            }
            .toList()
    }

    override fun getByName(authorName: String): List<JAuthor> {
        return dslContext.select()
            .from(AUTHOR)
            .where(AUTHOR.AUTHOR_NAME.like("%${authorName}%"))
            .fetch {
                it.into(JAuthor::class.java)
            }
            .toList()
    }

    override fun add(authorName: String): JAuthor? {
        // すでに同名の著者が登録済みである場合はエラー
        return dslContext
            .insertInto(AUTHOR, AUTHOR.AUTHOR_NAME)
            .values(authorName)
            .onConflict()
            .doNothing()
            .returningResult(AUTHOR.AUTHOR_ID, AUTHOR.AUTHOR_NAME)
            .fetchOne()
            ?.into(JAuthor::class.java)
    }

    override fun addOrGetExistsInfo(authorName: String): JAuthor {
        return dslContext
            .insertInto(AUTHOR, AUTHOR.AUTHOR_NAME)
            .values(authorName)
            .onConflict()
            .doNothing()
            .returningResult(AUTHOR.AUTHOR_ID, AUTHOR.AUTHOR_NAME)
            .fetchOne()
            ?.into(JAuthor::class.java)
            ?: get(authorName)!!
    }

    override fun edit(authorId: Int, authorName: String) {
        dslContext
            .update(AUTHOR)
            .set(AUTHOR.AUTHOR_NAME, authorName)
            .where(AUTHOR.AUTHOR_ID.eq(authorId))
            .execute()
    }

    override fun delete(authorId: Int) {
        dslContext
            .delete(AUTHOR)
            .where(AUTHOR.AUTHOR_ID.eq(authorId))
            .execute()
    }

    private fun get(authorName: String): JAuthor? {
        return dslContext.select()
            .from(AUTHOR)
            .where(AUTHOR.AUTHOR_NAME.eq(authorName))
            .fetchOne()
            ?.into(JAuthor::class.java)
    }
}