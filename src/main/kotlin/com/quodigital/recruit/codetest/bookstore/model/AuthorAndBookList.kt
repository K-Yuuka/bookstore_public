package com.quodigital.recruit.codetest.bookstore.model

/**
 * 著者と関連する書籍リスト
 */
data class AuthorAndBookList(
    /**
     * 著者
     */
    val author: AuthorInfo,
    /**
     * 関連する書籍リスト
     */
    val bookList: MutableList<BookInfo> = mutableListOf()
)
