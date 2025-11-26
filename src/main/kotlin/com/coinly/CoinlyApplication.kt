package com.coinly

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching

@SpringBootApplication
@EnableCaching
class CoinlyApplication

fun main(args: Array<String>) {
	runApplication<CoinlyApplication>(*args)
}
