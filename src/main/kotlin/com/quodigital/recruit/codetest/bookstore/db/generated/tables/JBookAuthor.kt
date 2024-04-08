/*
 * This file is generated by jOOQ.
 */
package com.quodigital.recruit.codetest.bookstore.db.generated.tables


import com.quodigital.recruit.codetest.bookstore.db.generated.JPublic
import com.quodigital.recruit.codetest.bookstore.db.generated.indexes.BOOK_AUTHOR_IX2
import com.quodigital.recruit.codetest.bookstore.db.generated.indexes.BOOK_AUTHOR_IX3
import com.quodigital.recruit.codetest.bookstore.db.generated.keys.BOOK_AUTHOR_IX1
import com.quodigital.recruit.codetest.bookstore.db.generated.keys.BOOK_AUTHOR_PKC
import com.quodigital.recruit.codetest.bookstore.db.generated.keys.BOOK_AUTHOR__BOOK_AUTHOR_FK1
import com.quodigital.recruit.codetest.bookstore.db.generated.keys.BOOK_AUTHOR__BOOK_AUTHOR_FK2
import com.quodigital.recruit.codetest.bookstore.db.generated.tables.JAuthor.JAuthorPath
import com.quodigital.recruit.codetest.bookstore.db.generated.tables.JBook.JBookPath
import com.quodigital.recruit.codetest.bookstore.db.generated.tables.records.JBookAuthorRecord

import kotlin.collections.Collection
import kotlin.collections.List

import org.jooq.Condition
import org.jooq.Field
import org.jooq.ForeignKey
import org.jooq.Index
import org.jooq.InverseForeignKey
import org.jooq.Name
import org.jooq.Path
import org.jooq.PlainSQL
import org.jooq.QueryPart
import org.jooq.Record
import org.jooq.SQL
import org.jooq.Schema
import org.jooq.Select
import org.jooq.Stringly
import org.jooq.Table
import org.jooq.TableField
import org.jooq.TableOptions
import org.jooq.UniqueKey
import org.jooq.impl.DSL
import org.jooq.impl.Internal
import org.jooq.impl.SQLDataType
import org.jooq.impl.TableImpl


/**
 * This class is generated by jOOQ.
 */
@Suppress("UNCHECKED_CAST")
open class JBookAuthor(
    alias: Name,
    path: Table<out Record>?,
    childPath: ForeignKey<out Record, JBookAuthorRecord>?,
    parentPath: InverseForeignKey<out Record, JBookAuthorRecord>?,
    aliased: Table<JBookAuthorRecord>?,
    parameters: Array<Field<*>?>?,
    where: Condition?
): TableImpl<JBookAuthorRecord>(
    alias,
    JPublic.PUBLIC,
    path,
    childPath,
    parentPath,
    aliased,
    parameters,
    DSL.comment(""),
    TableOptions.table(),
    where,
) {
    companion object {

        /**
         * The reference instance of <code>public.book_author</code>
         */
        val BOOK_AUTHOR: JBookAuthor = JBookAuthor()
    }

    /**
     * The class holding records for this type
     */
    override fun getRecordType(): Class<JBookAuthorRecord> = JBookAuthorRecord::class.java

    /**
     * The column <code>public.book_author.book_id</code>.
     */
    val BOOK_ID: TableField<JBookAuthorRecord, Int?> = createField(DSL.name("book_id"), SQLDataType.INTEGER.nullable(false), this, "")

    /**
     * The column <code>public.book_author.author_id</code>.
     */
    val AUTHOR_ID: TableField<JBookAuthorRecord, Int?> = createField(DSL.name("author_id"), SQLDataType.INTEGER.nullable(false), this, "")

    private constructor(alias: Name, aliased: Table<JBookAuthorRecord>?): this(alias, null, null, null, aliased, null, null)
    private constructor(alias: Name, aliased: Table<JBookAuthorRecord>?, parameters: Array<Field<*>?>?): this(alias, null, null, null, aliased, parameters, null)
    private constructor(alias: Name, aliased: Table<JBookAuthorRecord>?, where: Condition?): this(alias, null, null, null, aliased, null, where)

    /**
     * Create an aliased <code>public.book_author</code> table reference
     */
    constructor(alias: String): this(DSL.name(alias))

    /**
     * Create an aliased <code>public.book_author</code> table reference
     */
    constructor(alias: Name): this(alias, null)

    /**
     * Create a <code>public.book_author</code> table reference
     */
    constructor(): this(DSL.name("book_author"), null)

    constructor(path: Table<out Record>, childPath: ForeignKey<out Record, JBookAuthorRecord>?, parentPath: InverseForeignKey<out Record, JBookAuthorRecord>?): this(Internal.createPathAlias(path, childPath, parentPath), path, childPath, parentPath, BOOK_AUTHOR, null, null)

    /**
     * A subtype implementing {@link Path} for simplified path-based joins.
     */
    open class JBookAuthorPath : JBookAuthor, Path<JBookAuthorRecord> {
        constructor(path: Table<out Record>, childPath: ForeignKey<out Record, JBookAuthorRecord>?, parentPath: InverseForeignKey<out Record, JBookAuthorRecord>?): super(path, childPath, parentPath)
        private constructor(alias: Name, aliased: Table<JBookAuthorRecord>): super(alias, aliased)
        override fun `as`(alias: String): JBookAuthorPath = JBookAuthorPath(DSL.name(alias), this)
        override fun `as`(alias: Name): JBookAuthorPath = JBookAuthorPath(alias, this)
        override fun `as`(alias: Table<*>): JBookAuthorPath = JBookAuthorPath(alias.qualifiedName, this)
    }
    override fun getSchema(): Schema? = if (aliased()) null else JPublic.PUBLIC
    override fun getIndexes(): List<Index> = listOf(BOOK_AUTHOR_IX2, BOOK_AUTHOR_IX3)
    override fun getPrimaryKey(): UniqueKey<JBookAuthorRecord> = BOOK_AUTHOR_PKC
    override fun getUniqueKeys(): List<UniqueKey<JBookAuthorRecord>> = listOf(BOOK_AUTHOR_IX1)
    override fun getReferences(): List<ForeignKey<JBookAuthorRecord, *>> = listOf(BOOK_AUTHOR__BOOK_AUTHOR_FK2, BOOK_AUTHOR__BOOK_AUTHOR_FK1)

    private lateinit var _book: JBookPath

    /**
     * Get the implicit join path to the <code>public.book</code> table.
     */
    fun book(): JBookPath {
        if (!this::_book.isInitialized)
            _book = JBookPath(this, BOOK_AUTHOR__BOOK_AUTHOR_FK2, null)

        return _book;
    }

    val book: JBookPath
        get(): JBookPath = book()

    private lateinit var _author: JAuthorPath

    /**
     * Get the implicit join path to the <code>public.author</code> table.
     */
    fun author(): JAuthorPath {
        if (!this::_author.isInitialized)
            _author = JAuthorPath(this, BOOK_AUTHOR__BOOK_AUTHOR_FK1, null)

        return _author;
    }

    val author: JAuthorPath
        get(): JAuthorPath = author()
    override fun `as`(alias: String): JBookAuthor = JBookAuthor(DSL.name(alias), this)
    override fun `as`(alias: Name): JBookAuthor = JBookAuthor(alias, this)
    override fun `as`(alias: Table<*>): JBookAuthor = JBookAuthor(alias.qualifiedName, this)

    /**
     * Rename this table
     */
    override fun rename(name: String): JBookAuthor = JBookAuthor(DSL.name(name), null)

    /**
     * Rename this table
     */
    override fun rename(name: Name): JBookAuthor = JBookAuthor(name, null)

    /**
     * Rename this table
     */
    override fun rename(name: Table<*>): JBookAuthor = JBookAuthor(name.qualifiedName, null)

    /**
     * Create an inline derived table from this table
     */
    override fun where(condition: Condition?): JBookAuthor = JBookAuthor(qualifiedName, if (aliased()) this else null, condition)

    /**
     * Create an inline derived table from this table
     */
    override fun where(conditions: Collection<Condition>): JBookAuthor = where(DSL.and(conditions))

    /**
     * Create an inline derived table from this table
     */
    override fun where(vararg conditions: Condition?): JBookAuthor = where(DSL.and(*conditions))

    /**
     * Create an inline derived table from this table
     */
    override fun where(condition: Field<Boolean?>?): JBookAuthor = where(DSL.condition(condition))

    /**
     * Create an inline derived table from this table
     */
    @PlainSQL override fun where(condition: SQL): JBookAuthor = where(DSL.condition(condition))

    /**
     * Create an inline derived table from this table
     */
    @PlainSQL override fun where(@Stringly.SQL condition: String): JBookAuthor = where(DSL.condition(condition))

    /**
     * Create an inline derived table from this table
     */
    @PlainSQL override fun where(@Stringly.SQL condition: String, vararg binds: Any?): JBookAuthor = where(DSL.condition(condition, *binds))

    /**
     * Create an inline derived table from this table
     */
    @PlainSQL override fun where(@Stringly.SQL condition: String, vararg parts: QueryPart): JBookAuthor = where(DSL.condition(condition, *parts))

    /**
     * Create an inline derived table from this table
     */
    override fun whereExists(select: Select<*>): JBookAuthor = where(DSL.exists(select))

    /**
     * Create an inline derived table from this table
     */
    override fun whereNotExists(select: Select<*>): JBookAuthor = where(DSL.notExists(select))
}
