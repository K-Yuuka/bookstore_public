package com.quodigital.recruit.codetest.bookstore.api.controller.impl

import com.ninjasquad.springmockk.MockkBean
import com.quodigital.recruit.codetest.bookstore.TestUtils
import com.quodigital.recruit.codetest.bookstore.api.generated.model.Author
import com.quodigital.recruit.codetest.bookstore.api.generated.model.AuthorRequest
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

@WebMvcTest
class AuthorApiImplTest(@Autowired val mockMvc: MockMvc) {
    @MockkBean
    private lateinit var bookStoreService: BookStoreService

    @BeforeTest
    fun beforeTest() {
    }

    @Test
    fun authorGet_successful_emptyName() {
        every { bookStoreService.getAuthorListByName(null) } returns Result.success(
            TestUtils.buildAuthorList("AuthorRepositoryProvider_3.json")
        )
        every { bookStoreService.getAuthorListByName("name1") } returns Result.success(
            TestUtils.buildAuthorList("AuthorRepositoryProvider_1.json")
        )
        val result = this.mockMvc
            .perform(get("/author"))
            .andExpect(status().isOk)
            .andReturn()
        val actualResponse = TestUtils.jsonStringToObject<List<Author>>(result.response.contentAsString)
        assertEquals(3, actualResponse.size)
        assertEquals(1, actualResponse[0].id)
        assertEquals("name1", actualResponse[0].name)
        assertEquals(2, actualResponse[1].id)
        assertEquals("name2", actualResponse[1].name)
        assertEquals(3, actualResponse[2].id)
        assertEquals("name3", actualResponse[2].name)
    }

    @Test
    fun authorGet_successful_specifyName() {
        every { bookStoreService.getAuthorListByName(null) } returns Result.success(
            TestUtils.buildAuthorList("AuthorRepositoryProvider_3.json")
        )
        every { bookStoreService.getAuthorListByName("name1") } returns Result.success(
            TestUtils.buildAuthorList("AuthorRepositoryProvider_1.json")
        )
        val result = this.mockMvc
            .perform(get("/author?authorName=name1"))
            .andExpect(status().isOk)
            .andReturn()
        val actualResponse = TestUtils.jsonStringToObject<List<Author>>(result.response.contentAsString)
        assertEquals(1, actualResponse.size)
        assertEquals(1, actualResponse[0].id)
        assertEquals("name1", actualResponse[0].name)
    }

    @Test
    fun authorGet_failure_notFound() {
        val exceptionMessage = "test exception"
        every { bookStoreService.getAuthorListByName(any()) } returns Result.failure(NotFoundException(exceptionMessage))
        val result = this.mockMvc
            .perform(get("/author?authorName=name1"))
            .andExpect(status().isNotFound)
            .andReturn()
        assertEquals(exceptionMessage, TestUtils.jsonStringToObject<Error>(result.response.contentAsString).message)
    }

    @Test
    fun authorGet_failure_internalServerError() {
        val exceptionMessage = "test exception"
        every { bookStoreService.getAuthorListByName(any()) } returns Result.failure(
            NullPointerException(
                exceptionMessage
            )
        )
        val result = this.mockMvc
            .perform(get("/author?authorName=name1"))
            .andExpect(status().isInternalServerError)
            .andReturn()
        assertEquals(exceptionMessage, TestUtils.jsonStringToObject<Error>(result.response.contentAsString).message)
    }

    @Test
    fun authorPut_successful() {
        every { bookStoreService.editAuthorName(any(), any()) } returns Result.success(Unit)
        val requestJson = TestUtils.objectToJsonString(AuthorRequest("name1"))
        this.mockMvc
            .perform(
                put("/author/1")
                    .contentType("application/json")
                    .content(requestJson)
            )
            .andExpect(status().isNoContent)
            .andReturn()
    }

    @ParameterizedTest
    @NullAndEmptySource
    fun authorPut_failure_badRequest(authorName: String?) {
        every { bookStoreService.editAuthorName(any(), any()) } returns Result.success(Unit)
        val requestJson = TestUtils.objectToJsonString(AuthorRequest(authorName))
        val result = this.mockMvc
            .perform(
                put("/author/1")
                    .contentType("application/json")
                    .content(requestJson)
            )
            .andExpect(status().isBadRequest)
            .andReturn()
        assertFalse(TestUtils.jsonStringToObject<Error>(result.response.contentAsString).message.isNullOrBlank())
    }

    @Test
    fun authorPut_failure_notFound() {
        val exceptionMessage = "test exception"
        every { bookStoreService.editAuthorName(any(), any()) } returns Result.failure(
            NotFoundException(
                exceptionMessage
            )
        )
        val requestJson = TestUtils.objectToJsonString(AuthorRequest("name1"))
        val result = this.mockMvc
            .perform(
                put("/author/1")
                    .contentType("application/json")
                    .content(requestJson)
            )
            .andExpect(status().isNotFound)
            .andReturn()
        assertEquals(exceptionMessage, TestUtils.jsonStringToObject<Error>(result.response.contentAsString).message)
    }

    @Test
    fun authorPut_failure_conflict() {
        val exceptionMessage = "test exception"
        every { bookStoreService.editAuthorName(any(), any()) } returns Result.failure(
            ConflictException(
                exceptionMessage
            )
        )
        val requestJson = TestUtils.objectToJsonString(AuthorRequest("name1"))
        val result = this.mockMvc
            .perform(
                put("/author/1")
                    .contentType("application/json")
                    .content(requestJson)
            )
            .andExpect(status().isConflict)
            .andReturn()
        assertEquals(exceptionMessage, TestUtils.jsonStringToObject<Error>(result.response.contentAsString).message)
    }

    @Test
    fun authorPut_failure_internalServerError() {
        val exceptionMessage = "test exception"
        every { bookStoreService.editAuthorName(any(), any()) } returns Result.failure(
            NullPointerException(
                exceptionMessage
            )
        )
        val requestJson = TestUtils.objectToJsonString(AuthorRequest("name1"))
        val result = this.mockMvc
            .perform(
                put("/author/1")
                    .contentType("application/json")
                    .content(requestJson)
            )
            .andExpect(status().isInternalServerError)
            .andReturn()
        assertEquals(exceptionMessage, TestUtils.jsonStringToObject<Error>(result.response.contentAsString).message)
    }

    @Test
    fun authorDelete_successful() {
        every { bookStoreService.deleteAuthor(any()) } returns Result.success(Unit)
        this.mockMvc
            .perform(delete("/author/1"))
            .andExpect(status().isNoContent)
            .andReturn()
    }

    @Test
    fun authorDelete_failure_notFound() {
        val exceptionMessage = "test exception"
        every { bookStoreService.deleteAuthor(any()) } returns Result.failure(NotFoundException(exceptionMessage))
        val result = this.mockMvc
            .perform(delete("/author/1"))
            .andExpect(status().isNotFound)
            .andReturn()
        assertEquals(exceptionMessage, TestUtils.jsonStringToObject<Error>(result.response.contentAsString).message)
    }

    @Test
    fun authorDelete_failure_conflict() {
        val exceptionMessage = "test exception"
        every { bookStoreService.deleteAuthor(any()) } returns Result.failure(ConflictException(exceptionMessage))
        val result = this.mockMvc
            .perform(delete("/author/1"))
            .andExpect(status().isConflict)
            .andReturn()
        assertEquals(exceptionMessage, TestUtils.jsonStringToObject<Error>(result.response.contentAsString).message)
    }

    @Test
    fun authorDelete_failure_internalServerError() {
        val exceptionMessage = "test exception"
        every { bookStoreService.deleteAuthor(any()) } returns Result.failure(NullPointerException(exceptionMessage))
        val result = this.mockMvc
            .perform(delete("/author/1"))
            .andExpect(status().isInternalServerError)
            .andReturn()
        assertEquals(exceptionMessage, TestUtils.jsonStringToObject<Error>(result.response.contentAsString).message)
    }
}