package codetest.bookstore.service.impl

import codetest.bookstore.db.repository.AuthorRepository
import codetest.bookstore.db.repository.BookAuthorRepository
import codetest.bookstore.db.repository.BookRepository
import codetest.bookstore.service.BookStoreService
import com.ninjasquad.springmockk.SpykBean
import io.mockk.every
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.test.context.TestPropertySource
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue

@SpringBootTest
@TestPropertySource(locations = ["classpath:test.properties"])
class BookStoreServiceTransactionTest {
    @SpykBean
    private lateinit var bookAuthorRepository: BookAuthorRepository

    @SpykBean
    private lateinit var bookRepository: BookRepository

    @SpykBean
    private lateinit var authorRepository: AuthorRepository

    @Autowired
    private lateinit var service: BookStoreService

    @BeforeTest
    fun beforeTest() {
    }

    @Test
    fun addBookAndAuthor_failure_addAuthor() {
        every { bookAuthorRepository.editAuthor(any(), any()) } throws DataIntegrityViolationException("")

        val bookName = "addBookAndAuthor_failure_addAuthor"
        val authorName = "addBookAndAuthor_failure_addAuthor"

        runCatching {
            service.addBookAndAuthor(bookName, authorName)
        }.onFailure {
            assertTrue(authorRepository.getByName(authorName).isEmpty())
        }
    }

    @Test
    fun addBookAndAuthor_failure_addBookAuthor() {
        every { bookAuthorRepository.add(any(), any()) } throws DataIntegrityViolationException("")

        val bookName = "addBookAndAuthor_failure_addBookAuthor"
        val authorName = "addBookAndAuthor_failure_addBookAuthor"

        runCatching {
            service.addBookAndAuthor(bookName, authorName)
        }.onFailure {
            assertTrue(authorRepository.getByName(authorName).isEmpty())
            assertTrue(bookRepository.getByName(bookName).isEmpty())
        }
    }

    @Test
    fun editAuthor_failure_editAuthor() {
        every { bookAuthorRepository.editAuthor(any(), any()) } throws DataIntegrityViolationException("")

        val authorName = "addBookAndAuthor_failure_addBookAuthor"

        runCatching {
            service.editAuthor(1, authorName)
        }.onFailure {
            assertTrue(authorRepository.getByName(authorName).isEmpty())
        }
    }

}