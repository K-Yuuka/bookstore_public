package codetest.bookstore.db.repository.impl

const val ESCAPE_CHAR_FOR_LIKE_PREDICATE = "!"
fun escapeForLikePredicate(target: String): String =
    target.replace("%", "${ESCAPE_CHAR_FOR_LIKE_PREDICATE}%")
        .replace("_", "${ESCAPE_CHAR_FOR_LIKE_PREDICATE}_")
