package de.accso.mmrsic.camundakotlindemo

import org.camunda.bpm.engine.RepositoryService
import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication
import org.camunda.bpm.spring.boot.starter.event.PostDeployEvent
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.event.EventListener

/**
 * Sample Springboot Kotlin application.
 */
@SpringBootApplication
@EnableProcessApplication
open class CamundaKotlinDemoApplication(private val repositoryService: RepositoryService) {

    @EventListener
    fun processPostDeploy(evt: PostDeployEvent) {
        logger.debug("Post deploy event caught: $evt")
        val numProcessDefs = repositoryService.createProcessDefinitionQuery().list().size
        logger.info("Number of deployed process definitions: $numProcessDefs")
        logger.trace("The event knows about the repository too: ${evt.processEngine.repositoryService}")
    }
}

/** Main entry point for SpringBoot. */
fun main(args: Array<String>) {
    runApplication<CamundaKotlinDemoApplication>(*args)
}

/** The SLF4J logger of this instance. */
internal val Any.logger: Logger
    get() = LoggerFactory.getLogger(javaClass)