package com.quodigital.recruit.codetest.bookstore.service.impl

import com.quodigital.recruit.codetest.bookstore.db.generated.tables.pojos.JAuthor
import com.quodigital.recruit.codetest.bookstore.db.generated.tables.pojos.JBook
import com.quodigital.recruit.codetest.bookstore.db.repository.AuthorRepository
import com.quodigital.recruit.codetest.bookstore.db.repository.BookAuthorRepository
import com.quodigital.recruit.codetest.bookstore.db.repository.BookRepository
import com.quodigital.recruit.codetest.bookstore.db.repository.NameIdRepository
import com.quodigital.recruit.codetest.bookstore.exception.ConflictException
import com.quodigital.recruit.codetest.bookstore.exception.NotFoundException
import com.quodigital.recruit.codetest.bookstore.model.AuthorAndRelationalBooks
import com.quodigital.recruit.codetest.bookstore.model.AuthorInfo
import com.quodigital.recruit.codetest.bookstore.model.BookAuthorInfo
import com.quodigital.recruit.codetest.bookstore.model.BookInfo
import com.quodigital.recruit.codetest.bookstore.service.BookStoreService
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * 書籍サービス実装クラス
 */
@Service
@Transactional
class BookStoreServiceImpl(
    private val bookAuthorRepository: BookAuthorRepository,
    private val bookRepository: BookRepository,
    private val authorRepository: AuthorRepository
) : BookStoreService {
    override fun addBookAndAuthor(bookName: String, authorName: String): Result<BookAuthorInfo> {
        return kotlin.runCatching {
            require(bookName.isNotBlank()) { "bookName does not allow blanks." }
            require(authorName.isNotBlank()) { "authorName does not allow blanks." }
            return@runCatching when {
                bookAuthorRepository.exists(bookName, authorName) -> {
                    // 書籍名と著者名の組み合わせがすでに登録済みである場合はエラー
                    throw ConflictException("BookName='$bookName' and AuthorName='$authorName' have already registered.")
                }

                else -> {
                    val book = bookRepository.add(bookName)
                    val author = authorRepository.addOrGetExistsInfo(authorName)
                    try {
                        bookAuthorRepository.add(book.bookId!!, author.authorId!!)
                    } catch (ex: DataIntegrityViolationException) {
                        throw ConflictException(
                            "BookName='$bookName' and AuthorName='$authorName' have already registered.",
                            ex
                        )
                    }
                    BookAuthorInfo(toBook(book), toAuthor(author))
                }
            }
        }
    }

    override fun getBookListByName(bookName: String?): Result<List<BookInfo>> {
        return kotlin.runCatching {
            return@runCatching getByName(bookRepository, bookName).map { toBook(it) }.toList()
        }
    }

    override fun getBookListByAuthorName(authorName: String?): Result<List<AuthorAndRelationalBooks>> {
        return kotlin.runCatching {
            return@runCatching if (authorName != null) {
                toAuthorAndRelationalBooksList(bookAuthorRepository.getByAuthorName(authorName)
                    .ifEmpty { throw NotFoundException("No books by '$authorName' have been registered.") })
            } else {
                toAuthorAndRelationalBooksList(bookAuthorRepository.getAll())
            }
        }
    }

    override fun getAuthorListByName(authorName: String?): Result<List<AuthorInfo>> {
        return kotlin.runCatching {
            return@runCatching getByName(authorRepository, authorName).map { toAuthor(it) }.toList()
        }
    }

    override fun editBookName(bookId: Int, bookName: String): Result<Unit> = kotlin.runCatching {
        editName(bookRepository, bookId, bookName)
    }

    override fun editAuthorName(authorId: Int, authorName: String): Result<Unit> = kotlin.runCatching {
        editName(authorRepository, authorId, authorName)
    }

    override fun editAuthor(bookId: Int, authorName: String): Result<BookAuthorInfo> {
        return kotlin.runCatching {
            require(authorName.isNotBlank()) { "authorName does not allow blanks." }
            val targetBook = bookRepository.getById(bookId)
            check(targetBook != null) { "bookId='$bookId' does not exist." }
            val author = authorRepository.addOrGetExistsInfo(authorName)

            return@runCatching when {
                bookAuthorRepository.exists(targetBook.bookName!!, authorName) -> {
                    // 書籍と著者の組み合わせがすでに登録済みである場合はエラー
                    throw ConflictException(
                        "BookName='${targetBook.bookName}' and AuthorName='$authorName' have already registered."
                    )
                }

                else -> {
                    bookAuthorRepository.editAuthor(bookId, author.authorId!!)
                    BookAuthorInfo(
                        BookInfo(targetBook.bookId, targetBook.bookName),
                        AuthorInfo(author.authorId, author.authorName)
                    )
                }
            }
        }
    }

    override fun deleteBook(bookId: Int): Result<Unit> = kotlin.runCatching {
        delete(bookRepository, bookId)
    }

    override fun deleteAuthor(authorId: Int): Result<Unit> = kotlin.runCatching {
        delete(authorRepository, authorId)
    }

    private fun <T> getByName(repository: NameIdRepository<T>, name: String?): List<T> {
        return if (name.isNullOrBlank()) {
            repository.getAll()
        } else {
            repository.getByName(name).ifEmpty {
                throw NotFoundException("No data by '$name' have been registered.")
            }
        }
    }

    private fun <T> editName(repository: NameIdRepository<T>, id: Int, name: String) {
        try {
            require(name.isNotBlank()) { "Name does not allow blanks." }
            check(repository.exists(id)) { "Id='$id' does not exist." }
            repository.edit(id, name)
        } catch (ex: DataIntegrityViolationException) {
            throw ConflictException("Name = '$name' already exists.", ex)
        }
    }

    private fun <T> delete(repository: NameIdRepository<T>, id: Int) {
        check(repository.exists(id)) { "Id='$id' does not exist." }
        try {
            repository.delete(id)
        } catch (ex: DataIntegrityViolationException) {
            // FKエラー
            throw ConflictException("Id = '$id' is still referenced from other table", ex)
        }
    }

    private fun toBook(record: JBook): BookInfo = BookInfo(record.bookId, record.bookName)

    private fun toAuthor(record: JAuthor): AuthorInfo = AuthorInfo(record.authorId, record.authorName)

    private fun toAuthorAndRelationalBooksList(recordList: List<Pair<JAuthor, List<JBook>>>): List<AuthorAndRelationalBooks> =
        recordList.map { AuthorAndRelationalBooks(toAuthor(it.first), it.second.map { book -> toBook(book) }) }
            .toList()
}