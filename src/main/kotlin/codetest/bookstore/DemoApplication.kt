package codetest.bookstore

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

/**
 * アプリケーションメイン
 */
@SpringBootApplication
class DemoApplication

/**
 * メイン
 */
fun main(args: Array<String>) {
    runApplication<DemoApplication>(*args)
}
