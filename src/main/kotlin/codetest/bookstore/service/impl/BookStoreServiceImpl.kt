package codetest.bookstore.service.impl

import codetest.bookstore.db.generated.tables.pojos.JAuthor
import codetest.bookstore.db.generated.tables.pojos.JBook
import codetest.bookstore.db.repository.AuthorRepository
import codetest.bookstore.db.repository.BookAuthorRepository
import codetest.bookstore.db.repository.BookRepository
import codetest.bookstore.db.repository.NameIdRepository
import codetest.bookstore.exception.ConflictException
import codetest.bookstore.exception.NotFoundException
import codetest.bookstore.model.AuthorAndRelationalBooks
import codetest.bookstore.model.AuthorInfo
import codetest.bookstore.model.BookAuthorInfo
import codetest.bookstore.model.BookInfo
import codetest.bookstore.service.BookStoreService
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * 書籍サービス実装クラス
 */
@Service
@Transactional(rollbackFor = [Exception::class, RuntimeException::class])
class BookStoreServiceImpl(
    private val bookAuthorRepository: BookAuthorRepository,
    private val bookRepository: BookRepository,
    private val authorRepository: AuthorRepository
) : BookStoreService {
    override fun addBookAndAuthor(bookName: String, authorName: String): BookAuthorInfo {
        require(bookName.isNotBlank()) { "bookName does not allow blanks." }
        require(authorName.isNotBlank()) { "authorName does not allow blanks." }
        return when {
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

    override fun getBookListByName(bookName: String?): List<BookInfo> =
        getByName(bookRepository, bookName).map { toBook(it) }.toList()

    override fun getBookListByAuthorName(authorName: String?): List<AuthorAndRelationalBooks> {
        return if (authorName != null) {
            toAuthorAndRelationalBooksList(bookAuthorRepository.getByAuthorName(authorName)
                .ifEmpty { throw NotFoundException("No books by '$authorName' have been registered.") })
        } else {
            toAuthorAndRelationalBooksList(bookAuthorRepository.getAll())
        }
    }


    override fun getAuthorListByName(authorName: String?): List<AuthorInfo> =
        getByName(authorRepository, authorName).map { toAuthor(it) }.toList()

    override fun editBookName(bookId: Int, bookName: String): Unit =
        editName(bookRepository, bookId, bookName)

    override fun editAuthorName(authorId: Int, authorName: String): Unit =
        editName(authorRepository, authorId, authorName)

    override fun editAuthor(bookId: Int, authorName: String): BookAuthorInfo {
        require(authorName.isNotBlank()) { "authorName does not allow blanks." }
        val targetBook = bookRepository.getById(bookId)
            ?: throw NotFoundException("bookId='$bookId' does not exist.")
        val author = authorRepository.addOrGetExistsInfo(authorName)

        return when {
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

    override fun deleteBook(bookId: Int) = delete(bookRepository, bookId)

    override fun deleteAuthor(authorId: Int) = delete(authorRepository, authorId)

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
            if (!repository.exists(id)) throw NotFoundException("Id='$id' does not exist.")
            repository.edit(id, name)
        } catch (ex: DataIntegrityViolationException) {
            throw ConflictException("Name = '$name' already exists.", ex)
        }
    }

    private fun <T> delete(repository: NameIdRepository<T>, id: Int) {
        if (!repository.exists(id)) throw NotFoundException("Id='$id' does not exist.")
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