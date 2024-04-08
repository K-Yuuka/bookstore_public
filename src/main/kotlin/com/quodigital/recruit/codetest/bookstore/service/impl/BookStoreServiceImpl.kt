package com.quodigital.recruit.codetest.bookstore.service.impl

import com.quodigital.recruit.codetest.bookstore.db.generated.tables.pojos.JAuthor
import com.quodigital.recruit.codetest.bookstore.db.generated.tables.pojos.JBook
import com.quodigital.recruit.codetest.bookstore.db.repository.AuthorRepository
import com.quodigital.recruit.codetest.bookstore.db.repository.BookAuthorRepository
import com.quodigital.recruit.codetest.bookstore.db.repository.BookRepository
import com.quodigital.recruit.codetest.bookstore.exception.ConflictException
import com.quodigital.recruit.codetest.bookstore.exception.NotFoundException
import com.quodigital.recruit.codetest.bookstore.model.AuthorAndBookList
import com.quodigital.recruit.codetest.bookstore.model.AuthorInfo
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
    override fun getAuthorAndBookList(): List<AuthorAndBookList> = toAuthorAndBookList(bookAuthorRepository.getAll())

    override fun addBookAndAuthor(bookName: String, authorName: String): Pair<BookInfo, AuthorInfo> {
        if (bookAuthorRepository.exists(bookName, authorName)) {
            // 書籍と著者の組み合わせがすでに登録済みである場合はエラー
            throw ConflictException("BookName='$bookName' and AuthorName='$authorName' have already registered.")
        }
        val book = bookRepository.add(bookName)
        val author = authorRepository.addOrGetExistsInfo(authorName)
        bookAuthorRepository.add(book.bookId!!, author.authorId!!)
        return Pair(toBook(book), toAuthor(author))
    }

    override fun findBookByName(bookName: String): List<Pair<BookInfo, AuthorInfo>> {
        return bookRepository
            .getByName(bookName)
            .stream()
            .map {
                Pair(
                    BookInfo(it.first.bookId, it.first.bookName),
                    AuthorInfo(it.second.authorId, it.second.authorName)
                )
            }
            .toList()
    }

    @Throws(NotFoundException::class)
    override fun findBookByAuthorName(authorName: String): List<AuthorAndBookList> {
        val recordList = bookAuthorRepository.getByAuthorName(authorName)
        recordList.ifEmpty {
            throw NotFoundException("No books by '$authorName' have been registered.")
        }
        return toAuthorAndBookList(recordList)
    }

    override fun findAuthorByName(authorName: String): List<AuthorInfo> {
        return authorRepository
            .getByName(authorName)
            .stream()
            .map {
                AuthorInfo(it.authorId, it.authorName)
            }
            .toList()
    }

    override fun editBook(bookId: Int, bookName: String) {
        bookRepository.edit(bookId, bookName)
    }

    override fun editAuthor(authorId: Int, authorName: String) {
        authorRepository.edit(authorId, authorName)
    }

    override fun deleteAuthor(authorId: Int) {
        try {
            authorRepository.delete(authorId)
        } catch (ex: DataIntegrityViolationException) {
            // 著者が他の本に紐づいている
            throw ConflictException("author_id = '$authorId' is still referenced from other table", ex)
        }
    }

    override fun deleteBook(bookId: Int) {
        bookRepository.delete(bookId)
    }

    private fun buildAuthorBookSet(author: JAuthor): AuthorAndBookList {
        return AuthorAndBookList(AuthorInfo(author.authorId, author.authorName))
    }

    private fun toBookList(recordList: List<JBook>): List<BookInfo> = recordList.stream().map { toBook(it) }.toList()

    private fun toBook(record: JBook): BookInfo {
        return BookInfo(record.bookId, record.bookName)
    }

    private fun toAuthorList(recordList: List<JAuthor>): List<AuthorInfo> =
        recordList.stream().map { toAuthor(it) }.toList()

    private fun toAuthor(record: JAuthor): AuthorInfo {
        return AuthorInfo(record.authorId, record.authorName)
    }

    private fun toAuthorAndBookList(recordList: List<Pair<JAuthor, JBook>>): List<AuthorAndBookList> {
        val summarizeMap: MutableMap<Int, AuthorAndBookList> = mutableMapOf()
        recordList.stream()
            .forEach {
                if (it.first.authorId != null) {
                    val v = summarizeMap.getOrDefault(
                        it.first.authorId,
                        buildAuthorBookSet(it.first)
                    )
                    v.bookList.add(toBook(it.second))
                    summarizeMap.putIfAbsent(it.first.authorId!!, v)
                }
            }
        return summarizeMap.values.toList()
    }
//
//    private fun toModel(recordList: List<Pair<JAuthor, List<JBook>>>?): List<BookStore> {
//        val resultList: MutableList<BookInfo> = mutableListOf()
////        return recordList.stream().map {
////            toModel(it?.first, it?.second)
////        }.toList()
//        val res: MutableList<BookStore> = mutableListOf()
//        return res
//    }
//
//    private fun toModel(book: JBook, author: JAuthor): BookStore {
//        return BookStore(
//            bookId = book.bookId,
//            bookName = book.bookName,
//            authorId = author.authorId,
//            authorName = author.authorName
//        )
//    }
}