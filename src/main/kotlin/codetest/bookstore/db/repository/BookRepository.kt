package codetest.bookstore.db.repository

import codetest.bookstore.db.generated.tables.pojos.JAuthor
import codetest.bookstore.db.generated.tables.pojos.JBook

/**
 * 書籍レポジトリ
 */
interface BookRepository : NameIdRepository<JBook> {
}