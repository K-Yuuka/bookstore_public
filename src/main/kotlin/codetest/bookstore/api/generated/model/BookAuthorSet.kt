package codetest.bookstore.api.generated.model

import com.fasterxml.jackson.annotation.JsonProperty
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
)

