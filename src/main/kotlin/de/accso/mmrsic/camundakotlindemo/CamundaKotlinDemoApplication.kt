package de.accso.mmrsic.camundakotlindemo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

/**
 * Sample Springboot Kotlin application as presented at Camunda for Java.
 * @see <a href="https://docs.camunda.org/get-started/spring-boot/">Camunda Guide</a>
 */
@SpringBootApplication
open class CamundaKotlinDemoApplication

fun main(args: Array<String>) {
    runApplication<CamundaKotlinDemoApplication>(*args)
}