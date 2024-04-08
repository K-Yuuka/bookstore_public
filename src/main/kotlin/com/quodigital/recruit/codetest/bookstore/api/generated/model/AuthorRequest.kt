package com.quodigital.recruit.codetest.bookstore.api.generated.model

import java.util.Objects
import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema

/**
 * 
 * @param authorName 
 */
data class AuthorRequest(

    @Schema(example = "null", description = "")
    @get:JsonProperty("author_name") val authorName: kotlin.String? = null
) {

}

