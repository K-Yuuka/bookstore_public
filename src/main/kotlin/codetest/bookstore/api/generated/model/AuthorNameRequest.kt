package codetest.bookstore.api.generated.model

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema

/**
 *
 * @param authorName
 */
data class AuthorNameRequest(

    @Schema(example = "null", description = "")
    @get:JsonProperty("author_name") val authorName: String? = null
)

