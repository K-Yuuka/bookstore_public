package codetest.bookstore.db.repository.impl

import codetest.bookstore.db.repository.AuthorRepository
import codetest.bookstore.db.repository.BookAuthorRepository
import codetest.bookstore.db.repository.BookRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.test.context.TestPropertySource
import kotlin.test.Test
import kotlin.test.assertFailsWith

/**
 * Vx.xx__initial_data.sqlのデータが登録されていることを前提にテスト
 */
@SpringBootTest
@TestPropertySource(locations = ["classpath:test.properties"])
class BookAuthorRepositoryImplTest {
    @Autowired
    private lateinit var repository: BookAuthorRepository

    @Autowired
    private lateinit var bookRepository: BookRepository

    @Autowired
    private lateinit var authorRepository: AuthorRepository

    @Test
    fun getAll_successful() {
        val actual = repository.getAll()

        //
        val author1 = actual.first { it -> it.first.authorName.equals("夏目漱石") }
        assertEquals(3, author1.second.size)
        assertEquals("こころ", author1.second[0].bookName)
        assertEquals("坊っちゃん", author1.second[1].bookName)
        assertEquals("吾輩は猫である", author1.second[2].bookName)
        //
        val author2 = actual.first { it -> it.first.authorName.equals("東野圭吾") }
        assertEquals(3, author2.second.size)
        assertEquals("悪意", author2.second[0].bookName)
        assertEquals("秘密", author2.second[1].bookName)
        assertEquals("手紙", author2.second[2].bookName)
        //
        val author3 = actual.first { it -> it.first.authorName.equals("黒柳徹子") }
        assertEquals(1, author3.second.size)
        assertEquals("窓際のトットちゃん", author3.second[0].bookName)
        //
        val author4 = actual.first { it -> it.first.authorName.equals("西加奈子") }
        assertEquals(4, author4.second.size)
        assertEquals("サラバ!", author4.second[0].bookName)
        assertEquals("漁港の肉子ちゃん", author4.second[1].bookName)
        assertEquals("さくら", author4.second[2].bookName)
        assertEquals("きいろいゾウ", author4.second[3].bookName)
        //
        val author5 = actual.first { it -> it.first.authorName.equals("スティーヴン・キング") }
        assertEquals(2, author5.second.size)
        assertEquals("眠れる(上)", author5.second[0].bookName)
        assertEquals("眠れる(下)", author5.second[1].bookName)
        //
        val author6 = actual.first { it -> it.first.authorName.equals("オーウェン・キング") }
        assertEquals(2, author6.second.size)
        assertEquals("眠れる(上)", author6.second[0].bookName)
        assertEquals("眠れる(下)", author6.second[1].bookName)
    }

    @Test
    fun getByAuthorName_successful() {
        val actual = repository.getByAuthorName("キング")
        assertEquals(2, actual.size)
        //
        val author5 = actual.first { it -> it.first.authorName.equals("スティーヴン・キング") }
        assertEquals(2, author5.second.size)
        assertEquals("眠れる(上)", author5.second[0].bookName)
        assertEquals("眠れる(下)", author5.second[1].bookName)
        //
        val author6 = actual.first { it -> it.first.authorName.equals("オーウェン・キング") }
        assertEquals(2, author6.second.size)
        assertEquals("眠れる(上)", author6.second[0].bookName)
        assertEquals("眠れる(下)", author6.second[1].bookName)
    }

    @Test
    fun getByAuthorName_successful_notFound() {
        val actual = repository.getByAuthorName("山本太郎")
        assertEquals(0, actual.size)
    }

    @ParameterizedTest
    @CsvSource(value = [
        "%BookAuthorName%, %BookAuthorName",
        "_BookAuthorName_, _BookAuthorName"
    ])
    fun getByAuthorName_successful_needsEscape(targetValue:String, specifyName:String) {
        val addedAuthor = authorRepository.add(targetValue)
        val addedBook = bookRepository.add(targetValue)
        repository.add(addedBook.bookId!!, addedAuthor.authorId!!)

        val actual = repository.getByAuthorName(specifyName)
        kotlin.test.assertEquals(1, actual.size)
    }

    @Test
    fun exists_successful_true() {
        assertTrue(repository.exists("こころ", "夏目漱石"))
    }

    @Test
    fun exists_successful_false() {
        assertFalse(repository.exists("ほげほげ", "夏目漱石"))
    }

    @Test
    fun add_successful() {
        val addedBook = bookRepository.add("book1")
        val addedAuthor = authorRepository.add("name1")
        val actual = repository.add(addedBook.bookId!!, addedAuthor.authorId!!)
        assertEquals(addedBook.bookId, actual.bookId)
        assertEquals(addedAuthor.authorId, actual.authorId)
    }

    @Test
    fun add_failure_conflict() {
        val targetData = repository.getAll()[0]
        assertFailsWith<DataIntegrityViolationException> {
            repository.add(
                targetData.second[0].bookId!!,
                targetData.first.authorId!!
            )
        }
    }
    @Test
    fun editAuthor_successful() {
        val addedBook = bookRepository.add("editAuthor_successful_book")
        val addedAuthor = authorRepository.add("editAuthor_successful_name")
        repository.add(addedBook.bookId!!, addedAuthor.authorId!!)

        val newAuthor = authorRepository.add("editAuthor_successful_name2")
        assertTrue(repository.editAuthor(addedBook.bookId!!, newAuthor.authorId!!))
        assertEquals(newAuthor.authorName, repository.getByAuthorName(newAuthor.authorName!!)[0].first.authorName)
    }

    @Test
    fun editAuthor_failure_notFound_bookId() {
        val addedBook = bookRepository.add("editAuthor_failure_notFound_bookId_name")
        val addedAuthor = authorRepository.add("editAuthor_failure_notFound_bookId_name")
        repository.add(addedBook.bookId!!, addedAuthor.authorId!!)

        assertFalse(repository.editAuthor(99, addedAuthor.authorId!!))
    }

    @Test
    fun editAuthor_failure_notFound_authorId() {
        val addedBook = bookRepository.add("editAuthor_failure_notFound_authorId_name")
        val addedAuthor = authorRepository.add("editAuthor_failure_notFound_authorId_name")
        repository.add(addedBook.bookId!!, addedAuthor.authorId!!)
        assertFailsWith<DataIntegrityViolationException> {  repository.editAuthor(addedBook.bookId!!, 99)}
    }
}