package codetest.bookstore.db.repository.impl

import codetest.bookstore.db.repository.AuthorRepository
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.ValueSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.test.context.TestPropertySource
import kotlin.test.*

/**
 * Vx.xx__initial_data.sqlのデータが登録されていることを前提にテスト
 */
@SpringBootTest
@TestPropertySource(locations = ["classpath:test.properties"])
class AuthorRepositoryImplTest {
    @Autowired
    private lateinit var repository: AuthorRepository

    @Test
    fun getByName_successful_noRecord() {
        val actual = repository.getByName("noRecord")
        assertEquals(0, actual.size)
    }

    @Test
    fun getByName_successful_2Records() {
        val actual = repository.getByName("キング")
        assertEquals(2, actual.size)
    }

    @ParameterizedTest
    @CsvSource(value = [
        "%AuthorName%, %AuthorName",
        "_AuthorName_, _AuthorName"
    ])
    fun getByName_successful_needsEscape(targetValue:String, specifyName:String) {
        repository.add(targetValue)

        val actual = repository.getByName(specifyName)
        assertEquals(1, actual.size)
    }

    @Test
    fun getById_successful() {
        val actual = repository.getById(1)
        assertEquals(1, actual?.authorId)
        assertEquals("夏目漱石", actual?.authorName)
    }

    @Test
    fun getById_successful_notFound() {
        val actual = repository.getById(99)
        assertNull(actual)
    }

    @Test
    fun getAll_successful() {
        val actual = repository.getAll()
        assertTrue(actual.any { it.authorName.equals("夏目漱石") })
        assertTrue(actual.any { it.authorName.equals("東野圭吾") })
        assertTrue(actual.any { it.authorName.equals("黒柳徹子") })
        assertTrue(actual.any { it.authorName.equals("西加奈子") })
        assertTrue(actual.any { it.authorName.equals("スティーヴン・キング") })
        assertTrue(actual.any { it.authorName.equals("オーウェン・キング") })
    }

    @Test
    fun add_successful() {
        val expectedName = "山田太郎"
        val actual = repository.add(expectedName)
        assertEquals(expectedName, actual.authorName)
        assertNotNull(actual.authorId)
    }

    @Test
    fun add_failure_conflict() {
        assertFailsWith<DataIntegrityViolationException> { repository.add("黒柳徹子") }
    }

    @Test
    fun addOrGetExistsInfo_successful_addNew() {
        val beforeSize = repository.getAll().size
        val expectedName = "田中太郎"
        val actual = repository.addOrGetExistsInfo(expectedName)
        assertEquals(expectedName, actual.authorName)
        assertNotNull(actual.authorId)
        assertEquals(beforeSize + 1, repository.getAll().size)
    }

    @Test
    fun addOrGetExistsInfo_successful_exists() {
        val beforeSize = repository.getAll().size
        val expectedName = "黒柳徹子"
        val actual = repository.addOrGetExistsInfo(expectedName)
        assertEquals(expectedName, actual.authorName)
        assertNotNull(actual.authorId)
        assertEquals(beforeSize, repository.getAll().size)
    }

    @Test
    fun edit_successful() {
        val targetAuthor = repository.add("この著者を編集します")
        val expectedName = "山田花子"
        assertTrue(repository.edit(targetAuthor.authorId!!, expectedName))

        val actualAuthor = repository.getById(targetAuthor.authorId!!)
        assertEquals(expectedName, actualAuthor?.authorName)
        assertEquals(targetAuthor.authorId, actualAuthor?.authorId)
    }

    @Test
    fun edit_failure_notFound() {
        assertFalse(repository.edit(99, "田中太郎"))
    }

    @Test
    fun delete_successful() {
        val targetAuthor = repository.add("田中花子")
        assertTrue(repository.delete(targetAuthor.authorId!!))
        assertNull(repository.getById(targetAuthor.authorId!!))
    }

    @Test
    fun delete_failure_notFound() {
        assertFalse(repository.delete(99))
    }

    @Test
    fun delete_failure_conflict() {
        val targetAuthor = repository.getAll().first { it.authorName.equals("西加奈子") }
        assertFailsWith<DataIntegrityViolationException> { repository.delete(targetAuthor.authorId!!) }
    }

    @Test
    fun exists_successful_true() {
        assertTrue(repository.exists(repository.getAll().first().authorId!!))
    }

    @Test
    fun exists_successful_false() {
        assertFalse(repository.exists(99))
    }
}