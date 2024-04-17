package codetest.bookstore.api.generated.model

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema

/**
 *
 * @param bookName
 * @param authorName
 */
data class BookAndAuthorRequest(

    @Schema(example = "null", description = "")
    @get:JsonProperty("book_name") val bookName: String? = null,

    @Schema(example = "null", description = "")
    @get:JsonProperty("author_name") val authorName: String? = null
)

