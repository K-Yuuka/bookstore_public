package codetest.bookstore.exception

/**
 * 対象の情報が見つからない場合の例外
 */
class NotFoundException(
    override val message: String?
) : RuntimeException(message)