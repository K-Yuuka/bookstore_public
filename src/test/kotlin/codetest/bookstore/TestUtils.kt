package codetest.bookstore

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import codetest.bookstore.db.generated.tables.pojos.JAuthor
import codetest.bookstore.db.generated.tables.pojos.JBook
import codetest.bookstore.model.AuthorAndRelationalBooks
import codetest.bookstore.model.AuthorInfo
import codetest.bookstore.model.BookInfo
import codetest.bookstore.service.impl.BookStoreServiceImplTest
import java.nio.file.Path
import java.nio.file.Paths

object TestUtils {
    data class AuthorBookPair(val pair: Pair<JAuthor, List<JBook>>)

    fun getResourceFile(fileName: String, dir: String = "data"): Path =
        Paths.get(this.javaClass.classLoader.getResource("$dir/$fileName")?.toURI()!!)

    fun buildBookList(fileName: String): List<BookInfo> {
        return buildJBookList(fileName).map {
            BookInfo(it.bookId, it.bookName)
        }
    }

    fun buildAuthorList(fileName: String): List<AuthorInfo> {
        return buildJAuthorList(fileName).map {
            AuthorInfo(it.authorId, it.authorName)
        }
    }
//
//    fun buildJAuthorJBookPairList(fileName: String): List<Pair<JAuthor, List<JBook>>> {
//        return buildAuthorAndRelationalBooksList(fileName)
//            .map {
//                Pair(
//                    JAuthor(it.author.authorId, it.author.authorName) ,
//                    it.bookList.map { bookInfo ->  JBook(bookInfo.bookId, bookInfo.bookName)})
//            }.toList()
//    }
//
//    fun buildAuthorAndRelationalBooksList(fileName: String): List<AuthorAndRelationalBooks> {
//        return buildAuthorBookPairList(fileName).map {
//            AuthorAndRelationalBooks(
//                AuthorInfo(it.first.authorId, it.first.authorName),
//                it.second.map { jBook -> BookInfo(jBook.bookId, jBook.bookName) })
//        }
//    }

    fun buildJBookList(fileName: String): List<JBook> {
        val mapper = jacksonObjectMapper()
        return mapper.readValue<List<JBook>>(getResourceFile(fileName).toFile())
    }

    fun buildJAuthorList(fileName: String): List<JAuthor> {
        val mapper = jacksonObjectMapper()
        return mapper.readValue<List<JAuthor>>(getResourceFile(fileName).toFile())
    }

//    fun buildAuthorBookPairList(fileName: String): List<Pair<JAuthor, List<JBook>>> {
//        val mapper = jacksonObjectMapper()
//        return mapper.readValue<List<BookStoreServiceImplTest.AuthorBookPair>>(
//            getResourceFile(fileName).toFile()
//        ).map { it.pair }
//    }

    inline fun <reified T> jsonStringToObject(jsonString: String): T {
        val mapper = jacksonObjectMapper()
        return mapper.readValue<T>(jsonString)
    }

    inline fun <reified T> objectToJsonString(obj: T): String {
        val mapper = jacksonObjectMapper()
        return mapper.writeValueAsString(obj)
    }
}