package codetest.bookstore.api.generated.model

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema

/**
 *
 * @param message
 */
data class Error(

    @Schema(example = "null", description = "")
    @get:JsonProperty("message") val message: String? = null
)

