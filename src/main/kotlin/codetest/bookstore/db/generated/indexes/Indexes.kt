/*
 * This file is generated by jOOQ.
 */
package codetest.bookstore.db.generated.indexes


import codetest.bookstore.db.generated.tables.JAuthor
import codetest.bookstore.db.generated.tables.JBook
import codetest.bookstore.db.generated.tables.JBookAuthor

import org.jooq.Index
import org.jooq.impl.DSL
import org.jooq.impl.Internal


// -------------------------------------------------------------------------
// INDEX definitions
// -------------------------------------------------------------------------

val AUTHOR_IX3: Index =
    Internal.createIndex(DSL.name("author_ix3"), JAuthor.AUTHOR, arrayOf(JAuthor.AUTHOR.AUTHOR_ID), false)
val BOOK_AUTHOR_IX1: Index = Internal.createIndex(
    DSL.name("book_author_ix1"),
    JBookAuthor.BOOK_AUTHOR,
    arrayOf(JBookAuthor.BOOK_AUTHOR.AUTHOR_ID),
    false
)
val BOOK_AUTHOR_IX2: Index = Internal.createIndex(
    DSL.name("book_author_ix2"),
    JBookAuthor.BOOK_AUTHOR,
    arrayOf(JBookAuthor.BOOK_AUTHOR.BOOK_ID),
    false
)
val BOOK_IX1: Index = Internal.createIndex(DSL.name("book_ix1"), JBook.BOOK, arrayOf(JBook.BOOK.BOOK_NAME), false)
val BOOK_IX2: Index = Internal.createIndex(DSL.name("book_ix2"), JBook.BOOK, arrayOf(JBook.BOOK.BOOK_ID), false)
