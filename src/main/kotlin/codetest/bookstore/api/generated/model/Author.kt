package codetest.bookstore.api.generated.model

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema

/**
 *
 * @param id
 * @param name
 */
data class Author(

    @Schema(example = "null", description = "")
    @get:JsonProperty("id") val id: Int? = null,

    @Schema(example = "null", description = "")
    @get:JsonProperty("name") val name: String? = null
)

