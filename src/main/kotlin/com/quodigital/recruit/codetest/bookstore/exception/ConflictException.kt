package com.quodigital.recruit.codetest.bookstore.exception

/**
 * 書籍重複の例外
 */
class ConflictException(
    override val message: String?,
    val exception: Exception?
): RuntimeException(message) {
    constructor(message: String?) : this(message, null)
}