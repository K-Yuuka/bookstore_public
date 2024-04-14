package com.quodigital.recruit.codetest.bookstore.service.impl

import com.ninjasquad.springmockk.MockkBean
import com.quodigital.recruit.codetest.bookstore.TestUtils
import com.quodigital.recruit.codetest.bookstore.db.generated.tables.pojos.JAuthor
import com.quodigital.recruit.codetest.bookstore.db.generated.tables.pojos.JBook
import com.quodigital.recruit.codetest.bookstore.db.generated.tables.pojos.JBookAuthor
import com.quodigital.recruit.codetest.bookstore.db.repository.AuthorRepository
import com.quodigital.recruit.codetest.bookstore.db.repository.BookAuthorRepository
import com.quodigital.recruit.codetest.bookstore.db.repository.BookRepository
import com.quodigital.recruit.codetest.bookstore.exception.ConflictException
import com.quodigital.recruit.codetest.bookstore.exception.NotFoundException
import com.quodigital.recruit.codetest.bookstore.service.BookStoreService
import io.mockk.every
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EmptySource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.dao.DataIntegrityViolationException
import kotlin.test.*

@SpringBootTest
class BookStoreServiceImplTest {
    @MockkBean
    private lateinit var bookAuthorRepository: BookAuthorRepository

    @MockkBean
    private lateinit var bookRepository: BookRepository

    @MockkBean
    private lateinit var authorRepository: AuthorRepository

    @Autowired
    private lateinit var service: BookStoreService

    data class AuthorBookPair(val pair: Pair<JAuthor, List<JBook>>)

    @BeforeTest
    fun beforeTest() {
    }

    @Test
    fun addBookAndAuthor_successful() {
        val expectedBook = JBook(bookId = 1, bookName = "book1")
        val expectedAuthor = JAuthor(authorId = 2, authorName = "author2")
        every { bookAuthorRepository.exists(any(), any()) } returns false
        every { bookRepository.add(any()) } returns expectedBook
        every { authorRepository.addOrGetExistsInfo(any()) } returns expectedAuthor
        every { bookAuthorRepository.add(any(), any()) } returns JBookAuthor(
            expectedBook.bookId,
            expectedAuthor.authorId
        )

        service.addBookAndAuthor("book", "author")
            .onSuccess {
                assertEquals(expectedBook.bookId, it.bookInfo.bookId)
                assertEquals(expectedBook.bookName, it.bookInfo.bookName)
                assertEquals(expectedAuthor.authorId, it.authorInfo.authorId)
                assertEquals(expectedAuthor.authorName, it.authorInfo.authorName)
            }
            .onFailure {
                fail("Exception occurred.", it)
            }
    }

    @ParameterizedTest
    @EmptySource
    fun addBookAndAuthor_failure_blankBookName(bookName: String) {
        service.addBookAndAuthor(bookName, "author")
            .onSuccess {
                fail("No exception")
            }.onFailure {
                assertTrue(it is IllegalArgumentException)
            }
    }

    @ParameterizedTest
    @EmptySource
    fun addBookAndAuthor_failure_blankAuthorName(authorName: String) {
        service.addBookAndAuthor("book", authorName)
            .onSuccess {
                fail("No exception")
            }.onFailure {
                assertTrue(it is IllegalArgumentException)
            }
    }

    @Test
    fun addBookAndAuthor_failure_conflict() {
        every { bookAuthorRepository.exists(any(), any()) } returns true

        service.addBookAndAuthor("book", "author")
            .onSuccess {
                fail("No exception")
            }
            .onFailure {
                assertTrue(it is ConflictException)
            }
    }

    @Test
    fun getBookListByName_successful_nameIsNull() {
        every { bookRepository.getAll() } returns TestUtils.buildJBookList("BookRepositoryProvider_3.json")

        service.getBookListByName(null)
            .onSuccess {
                assertEquals(3, it.size)
                assertEquals(1, it[0].bookId)
                assertEquals("name1", it[0].bookName)
                assertEquals(2, it[1].bookId)
                assertEquals("name2", it[1].bookName)
                assertEquals(3, it[2].bookId)
                assertEquals("name3", it[2].bookName)
            }
            .onFailure {
                fail("Exception occurred.", it)
            }
    }

    @Test
    fun getBookListByName_successful_nameSpecify() {
        every { bookRepository.getByName("book1") } returns
                TestUtils.buildJBookList("BookRepositoryProvider_1.json")

        service.getBookListByName("book1")
            .onSuccess {
                assertEquals(1, it.size)
                assertEquals(1, it[0].bookId)
                assertEquals("name1", it[0].bookName)
            }
            .onFailure {
                fail("Exception occurred.", it)
            }
    }

    @Test
    fun getBookListByName_failure_nameSpecify_notFound() {
        every { bookRepository.getByName("book1") } returns listOf()

        service.getBookListByName("book1")
            .onSuccess {
                fail("No exception")
            }
            .onFailure {
                assertTrue(it is NotFoundException)
            }
    }

    @Test
    fun getBookListByAuthorName_successful_nameIsNull() {
        every { bookAuthorRepository.getAll() } returns
                TestUtils.buildJAuthorJBookPairList("BookAuthorRepositoryProvider_3.json")

        service.getBookListByAuthorName(null)
            .onSuccess {
                assertEquals(3, it.size)
                val actual1 = it[0]
                assertEquals(1, actual1.author.authorId)
                assertEquals("name1", actual1.author.authorName)
                assertEquals(2, actual1.bookList.size)
                assertEquals(1, actual1.bookList[0].bookId)
                assertEquals("name1", actual1.bookList[0].bookName)
                assertEquals(2, actual1.bookList[1].bookId)
                assertEquals("name2", actual1.bookList[1].bookName)

                val actual2 = it[1]
                assertEquals(2, actual2.author.authorId)
                assertEquals("name2", actual2.author.authorName)
                assertEquals(3, actual2.bookList.size)
                assertEquals(3, actual2.bookList[0].bookId)
                assertEquals("name3", actual2.bookList[0].bookName)
                assertEquals(4, actual2.bookList[1].bookId)
                assertEquals("name4", actual2.bookList[1].bookName)
                assertEquals(5, actual2.bookList[2].bookId)
                assertEquals("name5", actual2.bookList[2].bookName)

                val actual3 = it[2]
                assertEquals(3, actual3.author.authorId)
                assertEquals("name3", actual3.author.authorName)
                assertEquals(1, actual3.bookList.size)
                assertEquals(6, actual3.bookList[0].bookId)
                assertEquals("name6", actual3.bookList[0].bookName)
            }
            .onFailure {
                fail("Exception occurred.", it)
            }
    }

    @Test
    fun getBookListByAuthorName_successful_nameSpecify() {
        every { bookAuthorRepository.getByAuthorName("author1") } returns
                TestUtils.buildJAuthorJBookPairList("BookAuthorRepositoryProvider_1.json")

        service.getBookListByAuthorName("author1")
            .onSuccess {
                assertEquals(1, it.size)
                val actual1 = it[0]
                assertEquals(1, actual1.author.authorId)
                assertEquals("name1", actual1.author.authorName)
                assertEquals(2, actual1.bookList.size)
                assertEquals(1, actual1.bookList[0].bookId)
                assertEquals("name1", actual1.bookList[0].bookName)
                assertEquals(2, actual1.bookList[1].bookId)
                assertEquals("name2", actual1.bookList[1].bookName)
            }
            .onFailure {
                fail("Exception occurred.", it)
            }
    }

    @Test
    fun getBookListByAuthorName_successful_notFound() {
        every { bookAuthorRepository.getByAuthorName("author1") } returns listOf()

        service.getBookListByAuthorName("author1")
            .onSuccess {
                fail("No exception")
            }
            .onFailure {
                assertTrue(it is NotFoundException)
            }
    }

    @Test
    fun getAuthorListByName_successful_nameIsNull() {
        every { authorRepository.getAll() } returns TestUtils.buildJAuthorList("AuthorRepositoryProvider_3.json")

        service.getAuthorListByName(null)
            .onSuccess {
                assertEquals(3, it.size)
                assertEquals(1, it[0].authorId)
                assertEquals("name1", it[0].authorName)
                assertEquals(2, it[1].authorId)
                assertEquals("name2", it[1].authorName)
                assertEquals(3, it[2].authorId)
                assertEquals("name3", it[2].authorName)
            }
            .onFailure {
                fail("Exception occurred.", it)
            }
    }

    @Test
    fun getAuthorListByName_successful_nameSpecify() {
        every { authorRepository.getByName("author1") } returns
                TestUtils.buildJAuthorList("AuthorRepositoryProvider_1.json")

        service.getAuthorListByName("author1")
            .onSuccess {
                assertEquals(1, it.size)
                assertEquals(1, it[0].authorId)
                assertEquals("name1", it[0].authorName)
            }
            .onFailure {
                fail("Exception occurred.", it)
            }
    }

    @Test
    fun getAuthorListByName_failure_nameSpecify_notFound() {
        every { authorRepository.getByName("author1") } returns listOf()

        service.getAuthorListByName("author1")
            .onSuccess {
                fail("No exception")
            }
            .onFailure {
                assertTrue(it is NotFoundException)
            }
    }

    @Test
    fun editBookName_successful() {
        every { bookRepository.exists(any()) } returns true
        every { bookRepository.edit(any(), any()) } returns true

        service.editBookName(1, "book1")
            .onFailure {
                fail("Exception occurred.", it)
            }
    }

    @ParameterizedTest
    @EmptySource
    fun editBookName_failure_nameIsBlank(bookName: String) {
        service.editBookName(1, bookName)
            .onSuccess {
                fail("No exception")
            }
            .onFailure {
                assertTrue(it is IllegalArgumentException)
            }
    }

    @Test
    fun editBookName_failure_idNotExists() {
        every { bookRepository.exists(any()) } returns false

        service.editBookName(1, "book1")
            .onSuccess {
                fail("No exception")
            }
            .onFailure {
                assertTrue(it is IllegalStateException)
            }
    }

    @Test
    fun editBookName_failure_DataIntegrityViolationException() {
        every { bookRepository.exists(any()) } returns true
        every { bookRepository.edit(any(), any()) } throws DataIntegrityViolationException("")

        service.editBookName(1, "book1")
            .onSuccess {
                fail("No exception")
            }
            .onFailure {
                assertTrue(it is ConflictException)
            }
    }

    @Test
    fun editAuthorName_successful() {
        every { authorRepository.exists(any()) } returns true
        every { authorRepository.edit(any(), any()) } returns true

        service.editAuthorName(1, "author1")
            .onFailure {
                fail("Exception occurred.", it)
            }
    }

    @ParameterizedTest
    @EmptySource
    fun editAuthorName_failure_nameIsBlank(authorName: String) {
        service.editAuthorName(1, authorName)
            .onSuccess {
                fail("No exception")
            }
            .onFailure {
                assertTrue(it is IllegalArgumentException)
            }
    }

    @Test
    fun editAuthorName_failure_idNotExists() {
        every { authorRepository.exists(any()) } returns false

        service.editAuthorName(1, "author1")
            .onSuccess {
                fail("No exception")
            }
            .onFailure {
                assertTrue(it is IllegalStateException)
            }
    }

    @Test
    fun editAuthorName_failure_DataIntegrityViolationException() {
        every { authorRepository.exists(any()) } returns true
        every { authorRepository.edit(any(), any()) } throws DataIntegrityViolationException("")

        service.editAuthorName(1, "author1")
            .onSuccess {
                fail("No exception")
            }
            .onFailure {
                assertTrue(it is ConflictException)
            }
    }

    @Test
    fun editAuthor_successful() {
        val expectedBook = JBook(1, "book1")
        val expectedAuthor = JAuthor(2, "author1")
        every { bookRepository.getById(any()) } returns expectedBook
        every { authorRepository.addOrGetExistsInfo(any()) } returns expectedAuthor
        every { bookAuthorRepository.exists(any(), any()) } returns false
        every { bookAuthorRepository.editAuthor(any(), any()) } returns true

        service.editAuthor(1, "author1")
            .onSuccess {
                assertEquals(expectedBook.bookId, it.bookInfo.bookId)
                assertEquals(expectedBook.bookName, it.bookInfo.bookName)
                assertEquals(expectedAuthor.authorId, it.authorInfo.authorId)
                assertEquals(expectedAuthor.authorName, it.authorInfo.authorName)
            }
            .onFailure {
                fail("Exception occurred.", it)
            }
    }

    @ParameterizedTest
    @EmptySource
    fun editAuthor_failure_nameIsBlank(name: String) {
        service.editAuthor(1, name)
            .onSuccess {
                fail("No exception")
            }
            .onFailure {
                assertTrue(it is IllegalArgumentException)
            }
    }

    @Test
    fun editAuthor_failure_bookNotExists() {
        every { bookRepository.getById(any()) } returns null

        service.editAuthor(1, "author1")
            .onSuccess {
                fail("No exception")
            }
            .onFailure {
                assertTrue(it is IllegalStateException)
            }
    }

    @Test
    fun editAuthor_failure_conflictException() {
        val expectedBook = JBook(1, "book1")
        val expectedAuthor = JAuthor(2, "author1")
        every { bookRepository.getById(any()) } returns expectedBook
        every { authorRepository.addOrGetExistsInfo(any()) } returns expectedAuthor
        every { bookAuthorRepository.exists(any(), any()) } returns true

        service.editAuthor(1, "author1")
            .onSuccess {
                fail("No exception")
            }
            .onFailure {
                assertTrue(it is ConflictException)
            }
    }

    @Test
    fun deleteBook_successful() {
        every { bookRepository.exists(any()) } returns true
        every { bookRepository.delete(any()) } returns true

        service.deleteBook(1)
            .onFailure {
                fail("Exception occurred.", it)
            }
    }

    @Test
    fun deleteBook_failure_notExists() {
        every { bookRepository.exists(any()) } returns false

        service.deleteBook(1)
            .onSuccess {
                fail("No exception")
            }
            .onFailure {
                assertTrue(it is IllegalStateException)
            }
    }

    @Test
    fun deleteBook_failure_conflictException() {
        every { bookRepository.exists(any()) } returns true
        every { bookRepository.delete(any()) } throws DataIntegrityViolationException("")

        service.deleteBook(1)
            .onSuccess {
                fail("No exception")
            }
            .onFailure {
                assertTrue(it is ConflictException)
            }
    }

    @Test
    fun deleteAuthor_successful() {
        every { authorRepository.exists(any()) } returns true
        every { authorRepository.delete(any()) } returns true

        service.deleteAuthor(1)
            .onFailure {
                fail("Exception occurred.", it)
            }
    }

    @Test
    fun deleteAuthor_failure_notExists() {
        every { authorRepository.exists(any()) } returns false

        service.deleteAuthor(1)
            .onSuccess {
                fail("No exception")
            }
            .onFailure {
                assertTrue(it is IllegalStateException)
            }
    }

    @Test
    fun deleteAuthor_failure_conflictException() {
        every { authorRepository.exists(any()) } returns true
        every { authorRepository.delete(any()) } throws DataIntegrityViolationException("")

        service.deleteAuthor(1)
            .onSuccess {
                fail("No exception")
            }
            .onFailure {
                assertTrue(it is ConflictException)
            }
    }
}