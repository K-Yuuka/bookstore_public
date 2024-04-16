package codetest.bookstore.db.repository.impl

import codetest.bookstore.db.repository.BookAuthorRepository
import codetest.bookstore.db.repository.BookRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import kotlin.test.Test

/**
 * Vx.xx__initial_data.sqlのデータが登録されていることを前提にテスト
 */
@SpringBootTest
@TestPropertySource(locations = ["classpath:test.properties"])
class BookRepositoryImplTest {
    @Autowired
    private lateinit var repository: BookRepository

    @Autowired
    private lateinit var bookAuthorRepository: BookAuthorRepository

    @Test
    fun getByName_successful_noRecord() {
        val actual = repository.getByName("noRecord")
        assertEquals(0, actual.size)
    }

    @Test
    fun getByName_successful_2Records() {
        val actual = repository.getByName("眠れる")
        assertEquals(2, actual.size)
    }

    @ParameterizedTest
    @CsvSource(value = [
        "%BookName%, %BookName",
        "_BookName_, _BookName"
    ])
    fun getByName_successful_needsEscape(targetValue:String, specifyName:String) {
        repository.add(targetValue)

        val actual = repository.getByName(specifyName)
        kotlin.test.assertEquals(1, actual.size)
    }

    @Test
    fun getById_successful() {
        val actual = repository.getById(1)
        assertEquals(1, actual?.bookId)
        assertEquals("こころ", actual?.bookName)
    }

    @Test
    fun getById_successful_notFound() {
        val actual = repository.getById(99)
        assertNull(actual)
    }

    @Test
    fun getAll_successful() {
        val actual = repository.getAll()
        assertTrue(actual.any { it.bookName.equals("こころ") })
        assertTrue(actual.any { it.bookName.equals("坊っちゃん") })
        assertTrue(actual.any { it.bookName.equals("吾輩は猫である") })
        assertTrue(actual.any { it.bookName.equals("悪意") })
        assertTrue(actual.any { it.bookName.equals("秘密") })
        assertTrue(actual.any { it.bookName.equals("手紙") })
        assertTrue(actual.any { it.bookName.equals("窓際のトットちゃん") })
        assertTrue(actual.any { it.bookName.equals("サラバ!") })
        assertTrue(actual.any { it.bookName.equals("漁港の肉子ちゃん") })
        assertTrue(actual.any { it.bookName.equals("さくら") })
        assertTrue(actual.any { it.bookName.equals("きいろいゾウ") })
        assertTrue(actual.any { it.bookName.equals("眠れる(上)") })
        assertTrue(actual.any { it.bookName.equals("眠れる(下)") })
    }

    @Test
    fun add_successful() {
        val expectedName = "本1"
        val actual = repository.add(expectedName)
        assertEquals(expectedName, actual.bookName)
        assertNotNull(actual.bookId)
    }

    @Test
    fun add_successful_sameName() {
        val expectedName = "こころ"
        val actual = repository.add(expectedName)
        assertEquals(expectedName, actual.bookName)
        assertNotNull(actual.bookId)
    }

    @Test
    fun edit_successful() {
        val targetBook = repository.add("この本を編集します")
        val expectedName = "本2"
        assertTrue(repository.edit(targetBook.bookId!!, expectedName))

        val actual = repository.getById(targetBook.bookId!!)
        assertEquals(expectedName, actual?.bookName)
        assertEquals(targetBook.bookId, actual?.bookId)
    }

    @Test
    fun edit_failure_notFound() {
        assertFalse(repository.edit(99, "本2"))
    }

    @Test
    fun delete_successful() {
        val targetAuthor = repository.add("本3")
        assertTrue(repository.delete(targetAuthor.bookId!!))
        assertNull(repository.getById(targetAuthor.bookId!!))
    }

    @Test
    fun delete_failure_notFound() {
        assertFalse(repository.delete(99))
    }

    @Test
    fun delete_successful_relationalAuthor() {
        val targetBook = repository.add("本4")
        bookAuthorRepository.add(targetBook.bookId!!, 1)
        assertTrue(repository.delete(targetBook.bookId!!))
    }

    @Test
    fun exists_successful_true() {
        assertTrue(repository.exists(repository.getAll().first().bookId!!))
    }

    @Test
    fun exists_successful_false() {
        assertFalse(repository.exists(99))
    }
}