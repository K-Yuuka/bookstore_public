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
import kotlin.test.fail

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

        service.editAuthor(1, authorName).onFailure {
            assertTrue(authorRepository.getByName(authorName).isEmpty())
        }
    }
}