package de.accso.mmrsic.camundakotlindemo

import org.camunda.bpm.engine.ProcessEngineException
import org.camunda.bpm.engine.test.Deployment
import org.camunda.bpm.extension.junit5.test.ProcessEngineExtension
import org.camunda.bpm.scenario.ProcessScenario
import org.camunda.bpm.scenario.Scenario
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations


/** Process under test. */
private object Process {
    const val NAME = "Shopping"
    const val FILE_NAME = "shopping.bpmn"

    /** Activity IDs of [Process]. */
    object ActivityIds {
        const val COMPLETED = "ShoppingCompleted"
    }

    /** Variables of the [Process]. */
    object Variables {
        const val CART_NEEDED = "shoppingCartNeeded"
        const val CART_REQUIRED = "shoppingCartRequired"
        const val SHOPPING_LIST = "shoppingList"
        const val ALL_GOODS_BOUGHT = "allGoodsBought"

        /** Default values for [Variables]. */
        val DEFAULT_VALUES = mapOf(
            CART_NEEDED to false,
            CART_REQUIRED to false,
            SHOPPING_LIST to listOf<Any>(),
            ALL_GOODS_BOUGHT to true
        )

        /**All [Variables] of the [Process]. */
        val ALL = DEFAULT_VALUES.keys
    }
}

@ExtendWith(ProcessEngineExtension::class)
@Deployment(resources = ["processes/${Process.FILE_NAME}"])
class CamundaShoppingProcessTests {

    @Mock
    lateinit var shoppingProcess: ProcessScenario

    @BeforeEach
    fun initMocks() {
        MockitoAnnotations.openMocks(this)
    }

    @AfterEach
    fun resetMocks() {
        reset(shoppingProcess)
    }

    @Test
    fun testScenarioSimplestCompletionPath() {
        Scenario.run(shoppingProcess).startByKey(Process.NAME, Process.Variables.DEFAULT_VALUES).execute()
        verify(shoppingProcess).hasFinished(Process.ActivityIds.COMPLETED)

        verifyCompletedActivityIdsInOrder(
            shoppingProcess,
            "CreateShoppingList", "PrepareMeansOfPayment", "PrepareShoppingCompleted",
            "ChooseGoods#multiInstanceBody", "PayGoods", "PerformShoppingCompleted"
        )

        verifyNeverCompletedActivityIds(
            shoppingProcess,
            "PrepareShoppingCartDeposit", "TakeShoppingCart", "CreateNewShoppingList"
        )
    }

    /** Start a process instance where a required variable is not set. */
    @Test
    fun testStartProcessInstanceWithoutRequiredVariables() {
        assertThrows(ProcessEngineException::class.java) {
            Scenario.run(shoppingProcess).startByKey(Process.NAME).execute()
        }.apply {
            assertEquals(
                "Unknown property used in expression: \${!${Process.Variables.CART_NEEDED}}. Cause: Cannot resolve identifier '${Process.Variables.CART_NEEDED}'",
                message
            )
        }
    }

}

/** Verify that a given [ProcessScenario] Mockito mock has completed a given collection of activity IDs in the specified order. */
private fun verifyCompletedActivityIdsInOrder(processInstance: ProcessScenario, vararg activityIds: String) {
    val inOrderMock = inOrder(processInstance)
    for (activityId in activityIds) {
        inOrderMock.verify(processInstance).hasCompleted(activityId)
    }
}

/** Verify that a given [ProcessScenario] Mockito mock has never completed any of a given activity ID enumeration. */
private fun verifyNeverCompletedActivityIds(processScenario: ProcessScenario, vararg activityIds: String) {
    for (activityId in activityIds) {
        verify(processScenario, never()).hasCompleted(activityId)
    }
}

