/*
 * This file is generated by jOOQ.
 */
package codetest.bookstore.db.generated.tables


import codetest.bookstore.db.generated.JPublic
import codetest.bookstore.db.generated.indexes.BOOK_IX1
import codetest.bookstore.db.generated.indexes.BOOK_IX2
import codetest.bookstore.db.generated.keys.BOOK_AUTHOR__BOOK_AUTHOR_FK2
import codetest.bookstore.db.generated.keys.BOOK_PKC
import codetest.bookstore.db.generated.tables.JAuthor.JAuthorPath
import codetest.bookstore.db.generated.tables.JBookAuthor.JBookAuthorPath
import codetest.bookstore.db.generated.tables.records.JBookRecord
import org.jooq.*
import org.jooq.impl.DSL
import org.jooq.impl.Internal
import org.jooq.impl.SQLDataType
import org.jooq.impl.TableImpl


/**
 * This class is generated by jOOQ.
 */
@Suppress("UNCHECKED_CAST")
open class JBook(
    alias: Name,
    path: Table<out Record>?,
    childPath: ForeignKey<out Record, JBookRecord>?,
    parentPath: InverseForeignKey<out Record, JBookRecord>?,
    aliased: Table<JBookRecord>?,
    parameters: Array<Field<*>?>?,
    where: Condition?
) : TableImpl<JBookRecord>(
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
         * The reference instance of <code>public.book</code>
         */
        val BOOK: JBook = JBook()
    }

    /**
     * The class holding records for this type
     */
    override fun getRecordType(): Class<JBookRecord> = JBookRecord::class.java

    /**
     * The column <code>public.book.book_id</code>.
     */
    val BOOK_ID: TableField<JBookRecord, Int?> =
        createField(DSL.name("book_id"), SQLDataType.INTEGER.nullable(false).identity(true), this, "")

    /**
     * The column <code>public.book.book_name</code>.
     */
    val BOOK_NAME: TableField<JBookRecord, String?> =
        createField(DSL.name("book_name"), SQLDataType.VARCHAR(255).nullable(false), this, "")

    private constructor(alias: Name, aliased: Table<JBookRecord>?) : this(alias, null, null, null, aliased, null, null)
    private constructor(alias: Name, aliased: Table<JBookRecord>?, parameters: Array<Field<*>?>?) : this(
        alias,
        null,
        null,
        null,
        aliased,
        parameters,
        null
    )

    private constructor(alias: Name, aliased: Table<JBookRecord>?, where: Condition?) : this(
        alias,
        null,
        null,
        null,
        aliased,
        null,
        where
    )

    /**
     * Create an aliased <code>public.book</code> table reference
     */
    constructor(alias: String) : this(DSL.name(alias))

    /**
     * Create an aliased <code>public.book</code> table reference
     */
    constructor(alias: Name) : this(alias, null)

    /**
     * Create a <code>public.book</code> table reference
     */
    constructor() : this(DSL.name("book"), null)

    constructor(
        path: Table<out Record>,
        childPath: ForeignKey<out Record, JBookRecord>?,
        parentPath: InverseForeignKey<out Record, JBookRecord>?
    ) : this(Internal.createPathAlias(path, childPath, parentPath), path, childPath, parentPath, BOOK, null, null)

    /**
     * A subtype implementing {@link Path} for simplified path-based joins.
     */
    open class JBookPath : JBook, Path<JBookRecord> {
        constructor(
            path: Table<out Record>,
            childPath: ForeignKey<out Record, JBookRecord>?,
            parentPath: InverseForeignKey<out Record, JBookRecord>?
        ) : super(path, childPath, parentPath)

        private constructor(alias: Name, aliased: Table<JBookRecord>) : super(alias, aliased)

        override fun `as`(alias: String): JBookPath = JBookPath(DSL.name(alias), this)
        override fun `as`(alias: Name): JBookPath = JBookPath(alias, this)
        override fun `as`(alias: Table<*>): JBookPath = JBookPath(alias.qualifiedName, this)
    }

    override fun getSchema(): Schema? = if (aliased()) null else JPublic.PUBLIC
    override fun getIndexes(): List<Index> = listOf(BOOK_IX1, BOOK_IX2)
    override fun getIdentity(): Identity<JBookRecord, Int?> = super.getIdentity() as Identity<JBookRecord, Int?>
    override fun getPrimaryKey(): UniqueKey<JBookRecord> = BOOK_PKC

    private lateinit var _bookAuthor: JBookAuthorPath

    /**
     * Get the implicit to-many join path to the <code>public.book_author</code>
     * table
     */
    fun bookAuthor(): JBookAuthorPath {
        if (!this::_bookAuthor.isInitialized)
            _bookAuthor = JBookAuthorPath(this, null, BOOK_AUTHOR__BOOK_AUTHOR_FK2.inverseKey)

        return _bookAuthor
    }

    val bookAuthor: JBookAuthorPath
        get(): JBookAuthorPath = bookAuthor()

    /**
     * Get the implicit many-to-many join path to the <code>public.author</code>
     * table
     */
    val author: JAuthorPath
        get(): JAuthorPath = bookAuthor().author()

    override fun `as`(alias: String): JBook = JBook(DSL.name(alias), this)
    override fun `as`(alias: Name): JBook = JBook(alias, this)
    override fun `as`(alias: Table<*>): JBook = JBook(alias.qualifiedName, this)

    /**
     * Rename this table
     */
    override fun rename(name: String): JBook = JBook(DSL.name(name), null)

    /**
     * Rename this table
     */
    override fun rename(name: Name): JBook = JBook(name, null)

    /**
     * Rename this table
     */
    override fun rename(name: Table<*>): JBook = JBook(name.qualifiedName, null)

    /**
     * Create an inline derived table from this table
     */
    override fun where(condition: Condition?): JBook = JBook(qualifiedName, if (aliased()) this else null, condition)

    /**
     * Create an inline derived table from this table
     */
    override fun where(conditions: Collection<Condition>): JBook = where(DSL.and(conditions))

    /**
     * Create an inline derived table from this table
     */
    override fun where(vararg conditions: Condition?): JBook = where(DSL.and(*conditions))

    /**
     * Create an inline derived table from this table
     */
    override fun where(condition: Field<Boolean?>?): JBook = where(DSL.condition(condition))

    /**
     * Create an inline derived table from this table
     */
    @PlainSQL
    override fun where(condition: SQL): JBook = where(DSL.condition(condition))

    /**
     * Create an inline derived table from this table
     */
    @PlainSQL
    override fun where(@Stringly.SQL condition: String): JBook = where(DSL.condition(condition))

    /**
     * Create an inline derived table from this table
     */
    @PlainSQL
    override fun where(@Stringly.SQL condition: String, vararg binds: Any?): JBook =
        where(DSL.condition(condition, *binds))

    /**
     * Create an inline derived table from this table
     */
    @PlainSQL
    override fun where(@Stringly.SQL condition: String, vararg parts: QueryPart): JBook =
        where(DSL.condition(condition, *parts))

    /**
     * Create an inline derived table from this table
     */
    override fun whereExists(select: Select<*>): JBook = where(DSL.exists(select))

    /**
     * Create an inline derived table from this table
     */
    override fun whereNotExists(select: Select<*>): JBook = where(DSL.notExists(select))
}
