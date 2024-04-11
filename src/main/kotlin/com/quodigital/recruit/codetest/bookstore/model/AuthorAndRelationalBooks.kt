package com.quodigital.recruit.codetest.bookstore.model

/*** 著者と関連する書籍リスト */
data class AuthorAndRelationalBooks(val author: AuthorInfo, val bookList: List<BookInfo> = mutableListOf())