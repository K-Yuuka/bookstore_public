/*
 * This file is generated by jOOQ.
 */
package com.quodigital.recruit.codetest.bookstore.db.generated.tables.pojos


import java.io.Serializable


/**
 * This class is generated by jOOQ.
 */
@Suppress("UNCHECKED_CAST")
data class JAuthor(
    val authorId: Int? = null,
    val authorName: String? = null
): Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other)
            return true
        if (other == null)
            return false
        if (this::class != other::class)
            return false
        val o: JAuthor = other as JAuthor
        if (this.authorId == null) {
            if (o.authorId != null)
                return false
        }
        else if (this.authorId != o.authorId)
            return false
        if (this.authorName == null) {
            if (o.authorName != null)
                return false
        }
        else if (this.authorName != o.authorName)
            return false
        return true
    }

    override fun hashCode(): Int {
        val prime = 31
        var result = 1
        result = prime * result + (if (this.authorId == null) 0 else this.authorId.hashCode())
        result = prime * result + (if (this.authorName == null) 0 else this.authorName.hashCode())
        return result
    }

    override fun toString(): String {
        val sb = StringBuilder("JAuthor (")

        sb.append(authorId)
        sb.append(", ").append(authorName)

        sb.append(")")
        return sb.toString()
    }
}
