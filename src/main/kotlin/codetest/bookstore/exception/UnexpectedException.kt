package codetest.bookstore.exception

/**
 * 予期せぬ例外
 */
class UnexpectedException(
    override val message: String?
) : RuntimeException(message)