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
        LOG.debug("Post deploy event caught: $evt")
        val numProcessDefs = repositoryService.createProcessDefinitionQuery().list().size
        LOG.info("Number of deployed process definitions: $numProcessDefs")
        LOG.trace("The event knows about the repository too: ${evt.processEngine.repositoryService}")
    }
}

/** Main entry point for SpringBoot. */
fun main(args: Array<String>) {
    runApplication<CamundaKotlinDemoApplication>(*args)
}

/** Logger for this file. */
private val LOG: Logger = LoggerFactory.getLogger(CamundaKotlinDemoApplication::class.java.name)