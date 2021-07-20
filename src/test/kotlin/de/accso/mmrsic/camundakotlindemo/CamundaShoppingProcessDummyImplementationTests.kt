@file:Suppress("UnusedEquals")

package de.accso.mmrsic.camundakotlindemo

import de.accso.mmrsic.camundakotlindemo.Process.ActivityIds
import de.accso.mmrsic.camundakotlindemo.Process.Variables
import org.camunda.bpm.engine.ProcessEngine
import org.camunda.bpm.engine.runtime.ProcessInstance
import org.camunda.bpm.engine.test.assertions.ProcessEngineTests
import org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

/** Test cases where the dummy task implementations are activated in the Spring Boot application. */
@SpringBootTest
internal class CamundaShoppingProcessDummyImplementationTests {

    @Autowired
    lateinit var processEngine: ProcessEngine

    @BeforeEach
    fun initEachTestCase() {
        ProcessEngineTests.init(processEngine)
    }

    @AfterEach
    fun rollback() {
        ProcessEngineTests.reset()
    }

    @Test
    fun testAutowiredProcessEngine() {
        assertNotNull(processEngine)
    }

    @Test
    fun testStartProcessWithCartNeededButNotMandatory() {
        val processInstance: ProcessInstance = processEngine.runtimeService.startProcessInstanceByKey(
            Process.NAME, with(Variables) { mapOf(CART_NEEDED to true, CART_MANDATORY to false) }
        )
        assertThat(processInstance).isEnded
        assertThat(processInstance).hasNotPassed(ActivityIds.SHOPPING_FAILED)
        assertThat(processInstance).hasVariables(*Process.Variable.values().map { it.camundaName }.toTypedArray())
    }

    @Test
    fun testStartProcessWithCartDepositNotPreparedButCartMandatory() {
        val processInstance: ProcessInstance = processEngine.runtimeService.startProcessInstanceByKey(
            Process.NAME, with(Variables) { mapOf(CART_NEEDED to false, CART_MANDATORY to true) }
        )
        assertThat(processInstance).isEnded
            .hasPassed(ActivityIds.TAKE_CART)
            .hasNotPassed(ActivityIds.CHOOSE_GOODS)
            .hasNotPassed(ActivityIds.COMPLETED)
            .hasPassed(ActivityIds.SHOPPING_FAILED)
        assertThat(processInstance).variables()
            .hasEntrySatisfying("ShoppingErrorCode") { it == "CancelShoppingErrorCode" }
            .hasEntrySatisfying("ShoppingErrorMsg") { it == "CancelShoppingErrorMsg" }
    }
}