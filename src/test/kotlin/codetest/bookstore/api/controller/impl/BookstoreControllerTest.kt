package codetest.bookstore.api.controller.impl

import com.ninjasquad.springmockk.MockkBean
import codetest.bookstore.TestUtils
import codetest.bookstore.api.generated.model.*
import codetest.bookstore.exception.ConflictException
import codetest.bookstore.exception.NotFoundException
import codetest.bookstore.model.AuthorInfo
import codetest.bookstore.model.BookAuthorInfo
import codetest.bookstore.model.BookInfo
import codetest.bookstore.service.BookStoreService
import io.mockk.every
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.NullAndEmptySource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFalse

@WebMvcTest
class BookstoreControllerTest(@Autowired val mockMvc: MockMvc) {
    @MockkBean
    private lateinit var bookStoreService: BookStoreService

    @BeforeTest
    fun beforeTest() {
    }

    @Test
    fun bookstorePost_successful() {
        val expected = BookAuthorInfo(BookInfo(1, "name1"), AuthorInfo(2, "name2"))
        every { bookStoreService.addBookAndAuthor(any(), any()) } returns expected

        val result = this.mockMvc
            .perform(
                post("/bookstore")
                    .contentType("application/json")
                    .content(
                        TestUtils.objectToJsonString(
                            BookAndAuthorRequest(
                                bookName = expected.bookInfo.bookName,
                                authorName = expected.authorInfo.authorName
                            )
                        )
                    )
            )
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andReturn()
        val actualResponse = TestUtils.jsonStringToObject<BookAuthorSet>(result.response.contentAsString)
        assertEquals(expected.bookInfo.bookId, actualResponse.book?.id)
        assertEquals(expected.bookInfo.bookName, actualResponse.book?.name)
        assertEquals(expected.authorInfo.authorId, actualResponse.author?.id)
        assertEquals(expected.authorInfo.authorName, actualResponse.author?.name)
    }

    @ParameterizedTest
    @NullAndEmptySource
    fun bookstorePost_failure_badRequest_bookNameEmpty(bookName: String?) {
        val expected = BookAuthorInfo(BookInfo(1, bookName), AuthorInfo(2, "name2"))
        every { bookStoreService.addBookAndAuthor(any(), any()) } returns expected

        val result = this.mockMvc
            .perform(
                post("/bookstore")
                    .contentType("application/json")
                    .content(
                        TestUtils.objectToJsonString(
                            BookAndAuthorRequest(
                                bookName = expected.bookInfo.bookName,
                                authorName = expected.authorInfo.authorName
                            )
                        )
                    )
            )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andReturn()
        assertFalse(TestUtils.jsonStringToObject<Error>(result.response.contentAsString).message.isNullOrBlank())
    }

    @ParameterizedTest
    @NullAndEmptySource
    fun bookstorePost_failure_badRequest_authorNameEmpty(authorName: String?) {
        val expected = BookAuthorInfo(BookInfo(1, "name1"), AuthorInfo(2, authorName))
        every { bookStoreService.addBookAndAuthor(any(), any()) } returns expected

        val result = this.mockMvc
            .perform(
                post("/bookstore")
                    .contentType("application/json")
                    .content(
                        TestUtils.objectToJsonString(
                            BookAndAuthorRequest(
                                bookName = expected.bookInfo.bookName,
                                authorName = expected.authorInfo.authorName
                            )
                        )
                    )
            )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andReturn()
        assertFalse(TestUtils.jsonStringToObject<Error>(result.response.contentAsString).message.isNullOrBlank())
    }

    @Test
    fun bookstorePost_failure_conflict() {
        val exceptionMessage = "test exception"
        every { bookStoreService.addBookAndAuthor(any(), any()) } throws ConflictException( exceptionMessage)

        val result = this.mockMvc
            .perform(
                post("/bookstore")
                    .contentType("application/json")
                    .content(
                        TestUtils.objectToJsonString(
                            BookAndAuthorRequest(
                                bookName = "name1",
                                authorName = "name2"
                            )
                        )
                    )
            )
            .andExpect(MockMvcResultMatchers.status().isConflict)
            .andReturn()
        assertEquals(exceptionMessage, TestUtils.jsonStringToObject<Error>(result.response.contentAsString).message)
    }

    @Test
    fun bookstorePost_failure_internalServerError() {
        val exceptionMessage = "test exception"
        every { bookStoreService.addBookAndAuthor(any(), any()) } throws NullPointerException(exceptionMessage)

        val result = this.mockMvc
            .perform(
                post("/bookstore")
                    .contentType("application/json")
                    .content(
                        TestUtils.objectToJsonString(
                            BookAndAuthorRequest(
                                bookName = "name1",
                                authorName = "name2"
                            )
                        )
                    )
            )
            .andExpect(MockMvcResultMatchers.status().isInternalServerError)
            .andReturn()
        assertEquals(exceptionMessage, TestUtils.jsonStringToObject<Error>(result.response.contentAsString).message)
    }

    @Test
    fun bookstoreGet_successful_emptyName() {
        every { bookStoreService.getBookListByAuthorName(null) } returns
            TestUtils.buildAuthorAndRelationalBooksList("BookAuthorRepositoryProvider_3.json")
        every { bookStoreService.getBookListByAuthorName("name1") } returns
            TestUtils.buildAuthorAndRelationalBooksList("BookAuthorRepositoryProvider_1.json")
        val result = this.mockMvc
            .perform(MockMvcRequestBuilders.get("/bookstore"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()
        val actualResponse = TestUtils.jsonStringToObject<List<AuthorAndBookListInner>>(result.response.contentAsString)
        kotlin.test.assertEquals(3, actualResponse.size)

        val actual1 = actualResponse[0]
        kotlin.test.assertEquals(1, actual1.author?.id)
        kotlin.test.assertEquals("name1", actual1.author?.name)
        kotlin.test.assertEquals(2, actual1.book?.size)
        kotlin.test.assertEquals(1, actual1.book?.get(0)?.id)
        kotlin.test.assertEquals("name1", actual1.book?.get(0)?.name)
        kotlin.test.assertEquals(2, actual1.book?.get(1)?.id)
        kotlin.test.assertEquals("name2", actual1.book?.get(1)?.name)

        val actual2 = actualResponse[1]
        kotlin.test.assertEquals(2, actual2.author?.id)
        kotlin.test.assertEquals("name2", actual2.author?.name)
        kotlin.test.assertEquals(3, actual2.book?.size)
        kotlin.test.assertEquals(3, actual2.book?.get(0)?.id)
        kotlin.test.assertEquals("name3", actual2.book?.get(0)?.name)
        kotlin.test.assertEquals(4, actual2.book?.get(1)?.id)
        kotlin.test.assertEquals("name4", actual2.book?.get(1)?.name)
        kotlin.test.assertEquals(5, actual2.book?.get(2)?.id)
        kotlin.test.assertEquals("name5", actual2.book?.get(2)?.name)

        val actual3 = actualResponse[2]
        kotlin.test.assertEquals(3, actual3.author?.id)
        kotlin.test.assertEquals("name3", actual3.author?.name)
        kotlin.test.assertEquals(1, actual3.book?.size)
        kotlin.test.assertEquals(6, actual3.book?.get(0)?.id)
        kotlin.test.assertEquals("name6", actual3.book?.get(0)?.name)
    }

    @Test
    fun bookstoreGet_successful_specifyName() {
        every { bookStoreService.getBookListByAuthorName(null) } returns
            TestUtils.buildAuthorAndRelationalBooksList("BookAuthorRepositoryProvider_3.json")
        every { bookStoreService.getBookListByAuthorName("name1") } returns
            TestUtils.buildAuthorAndRelationalBooksList("BookAuthorRepositoryProvider_1.json")
        val result = this.mockMvc
            .perform(MockMvcRequestBuilders.get("/bookstore?authorName=name1"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()
        val actualResponse = TestUtils.jsonStringToObject<List<AuthorAndBookListInner>>(result.response.contentAsString)
        kotlin.test.assertEquals(1, actualResponse.size)

        val actual1 = actualResponse[0]
        kotlin.test.assertEquals(1, actual1.author?.id)
        kotlin.test.assertEquals("name1", actual1.author?.name)
        kotlin.test.assertEquals(2, actual1.book?.size)
        kotlin.test.assertEquals(1, actual1.book?.get(0)?.id)
        kotlin.test.assertEquals("name1", actual1.book?.get(0)?.name)
        kotlin.test.assertEquals(2, actual1.book?.get(1)?.id)
        kotlin.test.assertEquals("name2", actual1.book?.get(1)?.name)
    }

    @Test
    fun bookstoreGet_failure_notFound() {
        val exceptionMessage = "test exception"
        every { bookStoreService.getBookListByAuthorName(any()) } throws NotFoundException(exceptionMessage)
        val result = this.mockMvc
            .perform(MockMvcRequestBuilders.get("/bookstore?authorName=name1"))
            .andExpect(MockMvcResultMatchers.status().isNotFound)
            .andReturn()
        assertEquals(exceptionMessage, TestUtils.jsonStringToObject<Error>(result.response.contentAsString).message)
    }

    @Test
    fun bookstoreGet_failure_internalServerError() {
        val exceptionMessage = "test exception"
        every { bookStoreService.getBookListByAuthorName(any()) } throws NullPointerException(exceptionMessage)
        val result = this.mockMvc
            .perform(MockMvcRequestBuilders.get("/bookstore?authorName=name1"))
            .andExpect(MockMvcResultMatchers.status().isInternalServerError)
            .andReturn()
        assertEquals(exceptionMessage, TestUtils.jsonStringToObject<Error>(result.response.contentAsString).message)
    }

    @Test
    fun bookstoreBookIdPut_successful() {
        val expectedValue = BookAuthorInfo(BookInfo(1, "name1"), AuthorInfo(2, "name2"))
        every { bookStoreService.editAuthor(any(), any()) } returns expectedValue
        val requestJson = TestUtils.objectToJsonString(AuthorNameRequest("name1"))
        val result = this.mockMvc
            .perform(
                MockMvcRequestBuilders.put("/bookstore/1")
                    .contentType("application/json")
                    .content(requestJson)
            )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()
        val actualResponse = TestUtils.jsonStringToObject<BookAuthorSet>(result.response.contentAsString)
        assertEquals(expectedValue.bookInfo.bookId, actualResponse.book?.id)
        assertEquals(expectedValue.bookInfo.bookName, actualResponse.book?.name)
        assertEquals(expectedValue.authorInfo.authorId, actualResponse.author?.id)
        assertEquals(expectedValue.authorInfo.authorName, actualResponse.author?.name)
    }

    @ParameterizedTest
    @NullAndEmptySource
    fun bookstoreBookIdPut_failure_badRequest(authorName: String?) {
        val expectedValue = BookAuthorInfo(BookInfo(1, "name1"), AuthorInfo(2, "name2"))
        every { bookStoreService.editAuthor(any(), any()) } returns expectedValue
        val requestJson = TestUtils.objectToJsonString(AuthorNameRequest(authorName))
        val result = this.mockMvc
            .perform(
                MockMvcRequestBuilders.put("/bookstore/1")
                    .contentType("application/json")
                    .content(requestJson)
            )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andReturn()
        assertFalse(TestUtils.jsonStringToObject<Error>(result.response.contentAsString).message.isNullOrBlank())
    }

    @Test
    fun bookstoreBookIdPut_failure_notFound() {
        val exceptionMessage = "test exception"
        every { bookStoreService.editAuthor(any(), any()) } throws NotFoundException(exceptionMessage)
        val requestJson = TestUtils.objectToJsonString(AuthorNameRequest("name1"))
        val result = this.mockMvc
            .perform(
                MockMvcRequestBuilders.put("/bookstore/1")
                    .contentType("application/json")
                    .content(requestJson)
            )
            .andExpect(MockMvcResultMatchers.status().isNotFound)
            .andReturn()
        assertEquals(exceptionMessage, TestUtils.jsonStringToObject<Error>(result.response.contentAsString).message)
    }

    @Test
    fun bookstoreBookIdPut_failure_conflict() {
        val exceptionMessage = "test exception"
        every { bookStoreService.editAuthor(any(), any()) } throws ConflictException(exceptionMessage)
        val requestJson = TestUtils.objectToJsonString(AuthorNameRequest("name1"))
        val result = this.mockMvc
            .perform(
                MockMvcRequestBuilders.put("/bookstore/1")
                    .contentType("application/json")
                    .content(requestJson)
            )
            .andExpect(MockMvcResultMatchers.status().isConflict)
            .andReturn()
        assertEquals(exceptionMessage, TestUtils.jsonStringToObject<Error>(result.response.contentAsString).message)
    }

    @Test
    fun bookstoreBookIdPut_failure_internalServerError() {
        val exceptionMessage = "test exception"
        every {
            bookStoreService.editAuthor(
                any(),
                any()
            )
        } throws NullPointerException(exceptionMessage)
        val requestJson = TestUtils.objectToJsonString(AuthorNameRequest("name1"))
        val result = this.mockMvc
            .perform(
                MockMvcRequestBuilders.put("/bookstore/1")
                    .contentType("application/json")
                    .content(requestJson)
            )
            .andExpect(MockMvcResultMatchers.status().isInternalServerError)
            .andReturn()
        assertEquals(exceptionMessage, TestUtils.jsonStringToObject<Error>(result.response.contentAsString).message)
    }
}