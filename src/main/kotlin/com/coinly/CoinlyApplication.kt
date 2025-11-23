package com.coinly

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CoinlyApplication

fun main(args: Array<String>) {
	runApplication<CoinlyApplication>(*args)
}
