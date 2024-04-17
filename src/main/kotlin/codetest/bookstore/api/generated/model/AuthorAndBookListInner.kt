package codetest.bookstore.api.generated.model

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema

/**
 *
 * @param author
 * @param book
 */
data class AuthorAndBookListInner(

    @Schema(example = "null", description = "")
    @get:JsonProperty("author") val author: Author? = null,

    @Schema(example = "null", description = "")
    @get:JsonProperty("book") val book: List<Book>? = null
)

