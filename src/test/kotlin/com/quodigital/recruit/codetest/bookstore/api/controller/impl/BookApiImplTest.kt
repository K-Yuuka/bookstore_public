package com.quodigital.recruit.codetest.bookstore.api.controller.impl

import com.ninjasquad.springmockk.MockkBean
import com.quodigital.recruit.codetest.bookstore.TestUtils
import com.quodigital.recruit.codetest.bookstore.api.generated.model.Book
import com.quodigital.recruit.codetest.bookstore.api.generated.model.BookNameRequest
import com.quodigital.recruit.codetest.bookstore.api.generated.model.Error
import com.quodigital.recruit.codetest.bookstore.exception.ConflictException
import com.quodigital.recruit.codetest.bookstore.exception.NotFoundException
import com.quodigital.recruit.codetest.bookstore.service.BookStoreService
import io.mockk.every
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.NullAndEmptySource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import kotlin.test.*

@WebMvcTest
class BookApiImplTest(@Autowired val mockMvc: MockMvc) {
    @MockkBean
    private lateinit var bookStoreService: BookStoreService

    @BeforeTest
    fun beforeTest() {
    }

    @Test
    fun bookGet_successful_emptyName() {
        every { bookStoreService.getBookListByName(null) } returns Result.success(
            TestUtils.buildBookList("BookRepositoryProvider_3.json")
        )
        every { bookStoreService.getBookListByName("name1") } returns Result.success(
            TestUtils.buildBookList("BookRepositoryProvider_1.json")
        )
        val result = this.mockMvc
            .perform(MockMvcRequestBuilders.get("/book"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()
        val actualResponse = TestUtils.jsonStringToObject<List<Book>>(result.response.contentAsString)
        assertEquals(3, actualResponse.size)
        assertEquals(1, actualResponse[0].id)
        assertEquals("name1", actualResponse[0].name)
        assertEquals(2, actualResponse[1].id)
        assertEquals("name2", actualResponse[1].name)
        assertEquals(3, actualResponse[2].id)
        assertEquals("name3", actualResponse[2].name)
    }

    @Test
    fun bookGet_successful_specifyName() {
        every { bookStoreService.getBookListByName(null) } returns Result.success(
            TestUtils.buildBookList("BookRepositoryProvider_3.json")
        )
        every { bookStoreService.getBookListByName("name1") } returns Result.success(
            TestUtils.buildBookList("BookRepositoryProvider_1.json")
        )
        val result = this.mockMvc
            .perform(MockMvcRequestBuilders.get("/book?bookName=name1"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()
        val actualResponse = TestUtils.jsonStringToObject<List<Book>>(result.response.contentAsString)
        assertEquals(1, actualResponse.size)
        assertEquals(1, actualResponse[0].id)
        assertEquals("name1", actualResponse[0].name)
    }

    @Test
    fun bookGet_failure_notFound() {
        val exceptionMessage = "test exception"
        every { bookStoreService.getBookListByName(any()) } returns Result.failure(NotFoundException(exceptionMessage))
        val result = this.mockMvc
            .perform(MockMvcRequestBuilders.get("/book?bookName=name1"))
            .andExpect(MockMvcResultMatchers.status().isNotFound)
            .andReturn()
        assertEquals(exceptionMessage, TestUtils.jsonStringToObject<Error>(result.response.contentAsString).message)
    }

    @Test
    fun bookGet_failure_internalServerError() {
        val exceptionMessage = "test exception"
        every { bookStoreService.getBookListByName(any()) } returns Result.failure(NullPointerException(exceptionMessage))
        val result = this.mockMvc
            .perform(MockMvcRequestBuilders.get("/book?bookName=name1"))
            .andExpect(MockMvcResultMatchers.status().isInternalServerError)
            .andReturn()
        assertEquals(exceptionMessage, TestUtils.jsonStringToObject<Error>(result.response.contentAsString).message)
    }

    @Test
    fun bookPut_successful() {
        every { bookStoreService.editBookName(any(), any()) } returns Result.success(Unit)
        val requestJson = TestUtils.objectToJsonString(BookNameRequest("name1"))
        this.mockMvc
            .perform(
                MockMvcRequestBuilders.put("/book/1")
                    .contentType("application/json")
                    .content(requestJson)
            )
            .andExpect(MockMvcResultMatchers.status().isNoContent)
            .andReturn()
    }

    @ParameterizedTest
    @NullAndEmptySource
    fun bookPut_failure_badRequest(bookName: String?) {
        every { bookStoreService.editBookName(any(), any()) } returns Result.success(Unit)
        val requestJson = TestUtils.objectToJsonString(BookNameRequest(bookName))
        val result = this.mockMvc
            .perform(
                MockMvcRequestBuilders.put("/book/1")
                    .contentType("application/json")
                    .content(requestJson)
            )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andReturn()
        assertFalse(TestUtils.jsonStringToObject<Error>(result.response.contentAsString).message.isNullOrBlank())
    }

    @Test
    fun bookPut_failure_notFound() {
        val exceptionMessage = "test exception"
        every { bookStoreService.editBookName(any(), any()) } returns Result.failure(
            NotFoundException(
                exceptionMessage
            )
        )
        val requestJson = TestUtils.objectToJsonString(BookNameRequest("name1"))
        val result = this.mockMvc
            .perform(
                MockMvcRequestBuilders.put("/book/1")
                    .contentType("application/json")
                    .content(requestJson)
            )
            .andExpect(MockMvcResultMatchers.status().isNotFound)
            .andReturn()
        assertEquals(exceptionMessage, TestUtils.jsonStringToObject<Error>(result.response.contentAsString).message)
    }

    @Test
    fun bookPut_failure_conflict() {
        val exceptionMessage = "test exception"
        every { bookStoreService.editBookName(any(), any()) } returns Result.failure(
            ConflictException(
                exceptionMessage
            )
        )
        val requestJson = TestUtils.objectToJsonString(BookNameRequest("name1"))
        val result = this.mockMvc
            .perform(
                MockMvcRequestBuilders.put("/book/1")
                    .contentType("application/json")
                    .content(requestJson)
            )
            .andExpect(MockMvcResultMatchers.status().isConflict)
            .andReturn()
        assertEquals(exceptionMessage, TestUtils.jsonStringToObject<Error>(result.response.contentAsString).message)
    }

    @Test
    fun bookPut_failure_internalServerError() {
        val exceptionMessage = "test exception"
        every { bookStoreService.editBookName(any(), any()) } returns Result.failure(
            NullPointerException(
                exceptionMessage
            )
        )
        val requestJson = TestUtils.objectToJsonString(BookNameRequest("name1"))
        val result = this.mockMvc
            .perform(
                MockMvcRequestBuilders.put("/book/1")
                    .contentType("application/json")
                    .content(requestJson)
            )
            .andExpect(MockMvcResultMatchers.status().isInternalServerError)
            .andReturn()
        assertEquals(exceptionMessage, TestUtils.jsonStringToObject<Error>(result.response.contentAsString).message)
    }

    @Test
    fun bookDelete_successful() {
        every { bookStoreService.deleteBook(any()) } returns Result.success(Unit)
        this.mockMvc
            .perform(MockMvcRequestBuilders.delete("/book/1"))
            .andExpect(MockMvcResultMatchers.status().isNoContent)
            .andReturn()
    }

    @Test
    fun bookDelete_failure_notFound() {
        val exceptionMessage = "test exception"
        every { bookStoreService.deleteBook(any()) } returns Result.failure(NotFoundException(exceptionMessage))
        val result = this.mockMvc
            .perform(MockMvcRequestBuilders.delete("/book/1"))
            .andExpect(MockMvcResultMatchers.status().isNotFound)
            .andReturn()
        assertEquals(exceptionMessage, TestUtils.jsonStringToObject<Error>(result.response.contentAsString).message)
    }

    @Test
    fun bookDelete_failure_internalServerError() {
        val exceptionMessage = "test exception"
        every { bookStoreService.deleteBook(any()) } returns Result.failure(NullPointerException(exceptionMessage))
        val result = this.mockMvc
            .perform(MockMvcRequestBuilders.delete("/book/1"))
            .andExpect(MockMvcResultMatchers.status().isInternalServerError)
            .andReturn()
        assertEquals(exceptionMessage, TestUtils.jsonStringToObject<Error>(result.response.contentAsString).message)
    }

}