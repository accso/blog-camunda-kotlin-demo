package de.accso.mmrsic.camundakotlindemo

import de.accso.mmrsic.camundakotlindemo.task.*
import org.camunda.bpm.engine.ProcessEngineException
import org.camunda.bpm.engine.delegate.BpmnError
import org.camunda.bpm.engine.test.Deployment
import org.camunda.bpm.engine.test.mock.Mocks
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
import org.springframework.test.annotation.DirtiesContext

/** Test cases where all service task implementations are mocked. */
@ExtendWith(ProcessEngineExtension::class)
@Deployment(resources = ["processes/${Process.FILE_NAME}"])
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
internal class CamundaShoppingProcessMockTests {

    /** Mockito mocked [ProcessScenario] for unit tests. */
    @Mock
    lateinit var shoppingProcess: ProcessScenario

    @Mock
    lateinit var createShoppingListTask: CreateShoppingListTask
    @Mock
    lateinit var prepareMeansOfPaymentTask: PrepareMeansOfPaymentTask
    @Mock
    lateinit var prepareShoppingCartDepositTask: PrepareShoppingCartDepositTask
    @Mock
    lateinit var checkShoppingCartMandatoryTask: CheckShoppingCartMandatoryTask
    @Mock
    lateinit var takeShoppingCartTask: TakeShoppingCartTask
    @Mock
    lateinit var chooseGoodsTask: ChooseGoodsTask
    @Mock
    lateinit var payGoodsTask: PayGoodsTask
    @Mock
    lateinit var createNewShoppingListTask: CreateNewShoppingListTask

    /** Initialize all mocks of this JUnit class. */
    @BeforeEach
    fun initMocks() {
        MockitoAnnotations.openMocks(this)
        Mocks.register("createShoppingListTask", createShoppingListTask)
        Mocks.register("prepareMeansOfPaymentTask", prepareMeansOfPaymentTask)
        Mocks.register("prepareShoppingCartDepositTask", prepareShoppingCartDepositTask)
        Mocks.register("checkShoppingCartMandatoryTask", checkShoppingCartMandatoryTask)
        Mocks.register("takeShoppingCartTask", takeShoppingCartTask)
        Mocks.register("chooseGoodsTask", chooseGoodsTask)
        Mocks.register("payGoodsTask", payGoodsTask)
        Mocks.register("createNewShoppingListTask", createNewShoppingListTask)
    }

    /** Reset all [Mock]s of this JUnit class. */
    @AfterEach
    fun resetMocks() {
        reset(shoppingProcess)
        reset(createShoppingListTask)
        reset(prepareMeansOfPaymentTask)
        reset(prepareShoppingCartDepositTask)
        reset(takeShoppingCartTask)
        reset(chooseGoodsTask)
        reset(payGoodsTask)
        reset(createNewShoppingListTask)
    }

    /** Test the [shoppingProcess] execution with all necessary values providing the simplest execution path. */
    @Test
    fun testScenarioSimplestCompletionPath() {
        Scenario.run(shoppingProcess).startByKey(Process.NAME, Process.Variables.DEFAULT_VALUES).execute()

        with(Process.ActivityIds) {
            verify(shoppingProcess).hasFinished(COMPLETED)
            verifyCompletedActivityIdsInOrder(
                shoppingProcess,
                CREATE_SHOPPING_LIST, PREPARE_MEANS_OF_PAYMENT, PREPARE_SHOPPING_COMPLETED,
                CHOOSE_GOODS, PAY_GOODS, SHOPPING_COMPLETED
            )
            verifyNeverCompletedActivityIds(shoppingProcess, PREPARE_CART_DEPOSIT, TAKE_CART, CREATE_NEW_SHOPPING_LIST)
        }
    }

    /** Test the [shoppingProcess] execution with with an alternative path as imposed by variable values. */
    @Test
    fun testScenarioShoppingCartNeededAndRequired() {
        Scenario.run(shoppingProcess).startByKey(
            Process.NAME, Process.Variables.DEFAULT_VALUES.plus(
                mapOf(Process.Variables.CART_NEEDED to true, Process.Variables.CART_MANDATORY to true)
            )
        ).execute()

        with(Process.ActivityIds) {
            verify(shoppingProcess).hasFinished(COMPLETED)
            verifyCompletedActivityIdsInOrder(
                shoppingProcess,
                CREATE_SHOPPING_LIST, PREPARE_MEANS_OF_PAYMENT, PREPARE_CART_DEPOSIT, PREPARE_SHOPPING_COMPLETED,
                TAKE_CART, CHOOSE_GOODS, PAY_GOODS, SHOPPING_COMPLETED,
                COMPLETED
            )
            verifyNeverCompletedActivityIds(shoppingProcess, CREATE_NEW_SHOPPING_LIST)
        }
    }

    @Test
    fun testScenarioShoppingCartNeededButNotRequired() {
        Scenario.run(shoppingProcess).startByKey(
            Process.NAME, Process.Variables.DEFAULT_VALUES.plus(
                mapOf(Process.Variables.CART_NEEDED to true, Process.Variables.CART_MANDATORY to false)
            )
        ).execute()

        with(Process.ActivityIds) {
            verify(shoppingProcess).hasFinished(COMPLETED)
            verifyCompletedActivityIdsInOrder(
                shoppingProcess,
                CREATE_SHOPPING_LIST, PREPARE_MEANS_OF_PAYMENT, PREPARE_CART_DEPOSIT, PREPARE_SHOPPING_COMPLETED,
                TAKE_CART, CHOOSE_GOODS, PAY_GOODS, SHOPPING_COMPLETED,
                COMPLETED
            )
            verifyNeverCompletedActivityIds(shoppingProcess, CREATE_NEW_SHOPPING_LIST)
        }
    }

    @Test
    fun testScenarioShoppingCartNotNeededButRequired() {
        Scenario.run(shoppingProcess).startByKey(
            Process.NAME, Process.Variables.DEFAULT_VALUES.plus(
                mapOf(Process.Variables.CART_NEEDED to false, Process.Variables.CART_MANDATORY to true)
            )
        ).execute()

        with(Process.ActivityIds) {
            verify(shoppingProcess).hasFinished(COMPLETED)
            verifyCompletedActivityIdsInOrder(
                shoppingProcess,
                CREATE_SHOPPING_LIST, PREPARE_MEANS_OF_PAYMENT, PREPARE_SHOPPING_COMPLETED,
                TAKE_CART, CHOOSE_GOODS, PAY_GOODS, SHOPPING_COMPLETED,
                COMPLETED
            )
            verifyNeverCompletedActivityIds(shoppingProcess, PREPARE_CART_DEPOSIT, CREATE_NEW_SHOPPING_LIST)
        }
    }

    @Test
    fun testScenarioShoppingCartNotNeededNorRequired() {
        Scenario.run(shoppingProcess).startByKey(
            Process.NAME, Process.Variables.DEFAULT_VALUES.plus(
                mapOf(Process.Variables.CART_NEEDED to false, Process.Variables.CART_MANDATORY to false)
            )
        ).execute()

        with(Process.ActivityIds) {
            verify(shoppingProcess).hasFinished(COMPLETED)
            verifyCompletedActivityIdsInOrder(
                shoppingProcess,
                CREATE_SHOPPING_LIST, PREPARE_MEANS_OF_PAYMENT, PREPARE_SHOPPING_COMPLETED,
                CHOOSE_GOODS, PAY_GOODS, SHOPPING_COMPLETED,
                COMPLETED
            )
            verifyNeverCompletedActivityIds(shoppingProcess, PREPARE_CART_DEPOSIT, TAKE_CART, CREATE_NEW_SHOPPING_LIST)
        }
    }

    /** Test whether throwing a [BpmnError] when executing the [PrepareMeansOfPaymentTask] results in completing
     * [Process.ActivityIds.PREPARATION_FAILED]. */
    @Test
    fun testScenarioMeansOfPaymentPreparationBpmnError() {
        `when`(prepareMeansOfPaymentTask.execute(any())).thenThrow(BpmnError("IntentionalTestError"))
        Scenario.run(shoppingProcess).startByKey(Process.NAME, Process.Variables.DEFAULT_VALUES).execute()
        verify(shoppingProcess).hasCompleted(Process.ActivityIds.PREPARATION_FAILED)
    }

    @Test
    fun testScenarioMeansOfPaymentPreparationException() {
        `when`(prepareMeansOfPaymentTask.execute(any())).thenThrow(RuntimeException("IntentionalTestException"))
        assertThrows(RuntimeException::class.java) {
            Scenario.run(shoppingProcess).startByKey(Process.NAME, Process.Variables.DEFAULT_VALUES).execute()
        }.apply { assertEquals("IntentionalTestException", message) { "Runtime exception message" } }
    }

    @Test
    fun testScenarioShoppingListEmpty() {
        Scenario.run(shoppingProcess).startByKey(
            Process.NAME, Process.Variables.DEFAULT_VALUES.plus(Process.Variables.SHOPPING_LIST to listOf<Any>())
        ).execute()

        verify(shoppingProcess).hasCompleted(Process.ActivityIds.COMPLETED)
    }

    /** Test the [shoppingProcess] execution when at least a required variable is not set. */
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

// HELPERS //

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

