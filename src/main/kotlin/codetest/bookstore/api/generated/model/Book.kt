package codetest.bookstore.api.generated.model

import java.util.Objects
import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema

/**
 * 
 * @param id 
 * @param name 
 */
data class Book(

    @Schema(example = "null", description = "")
    @get:JsonProperty("id") val id: kotlin.Int? = null,

    @Schema(example = "null", description = "")
    @get:JsonProperty("name") val name: kotlin.String? = null
) {

}

