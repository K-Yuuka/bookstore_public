package codetest.bookstore.api.generated.model

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema

/**
 *
 * @param bookName
 */
data class BookNameRequest(

    @Schema(example = "null", description = "")
    @get:JsonProperty("book_name") val bookName: String? = null
)

