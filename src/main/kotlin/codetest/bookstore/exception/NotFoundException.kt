package codetest.bookstore.exception

/**
 * 書籍重複の例外
 */
class NotFoundException(
    override val message: String?
): RuntimeException(message)