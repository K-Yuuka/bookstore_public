package com.quodigital.recruit.codetest.bookstore.api.generated.model

import java.util.Objects
import com.fasterxml.jackson.annotation.JsonProperty
import com.quodigital.recruit.codetest.bookstore.api.generated.model.Author
import com.quodigital.recruit.codetest.bookstore.api.generated.model.Book
import io.swagger.v3.oas.annotations.media.Schema

/**
 * 
 * @param book 
 * @param author 
 */
data class BookAuthorSet(

    @Schema(example = "null", description = "")
    @get:JsonProperty("book") val book: Book? = null,

    @Schema(example = "null", description = "")
    @get:JsonProperty("author") val author: Author? = null
) {

}

