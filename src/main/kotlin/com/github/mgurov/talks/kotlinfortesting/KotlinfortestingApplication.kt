package com.github.mgurov.talks.kotlinfortesting

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class KotlinfortestingApplication

fun main(args: Array<String>) {
	runApplication<KotlinfortestingApplication>(*args)
}
