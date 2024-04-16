package codetest.bookstore.api.exception

open class HttpException(
    override val message: String?,
    override val cause: Throwable?
) : RuntimeException(message, cause) {
    constructor(message: String?) : this(message, null)
    constructor(cause: Throwable) : this(cause.message, cause)
}