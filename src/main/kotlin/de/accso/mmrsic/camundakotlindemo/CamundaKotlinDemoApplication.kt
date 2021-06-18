package de.accso.mmrsic.camundakotlindemo

import org.camunda.bpm.engine.RuntimeService
import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication
import org.camunda.bpm.spring.boot.starter.event.PostDeployEvent
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.event.EventListener
import java.util.logging.Logger

/**
 * Sample Springboot Kotlin application as presented at Camunda for Java.
 * @see <a href="https://docs.camunda.org/get-started/spring-boot/">Camunda Guide</a>
 */
@SpringBootApplication
@EnableProcessApplication
open class CamundaKotlinDemoApplication(private val runtimeService: RuntimeService) {

    @EventListener
    fun processPostDeploy(evt: PostDeployEvent) {
        LOG.info("Post deploy event caught: $evt")
        val processInstance = runtimeService.startProcessInstanceByKey("loanApproval")
        LOG.info("Started process instance with definition ID=${processInstance.processDefinitionId}")
    }
}

fun main(args: Array<String>) {
    runApplication<CamundaKotlinDemoApplication>(*args)
}

private val LOG: Logger = Logger.getLogger(CamundaKotlinDemoApplication::class.java.name)