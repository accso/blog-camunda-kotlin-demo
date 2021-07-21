package de.accso.mmrsic.camundakotlindemo.task.dummy

import de.accso.mmrsic.camundakotlindemo.Process
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.junit.jupiter.api.Test
import org.mockito.Mockito

/** Test cases for [CreateRandomShoppingList]. */
internal class CreateRandomShoppingListTest {

    @Test
    fun testExecutionCreatesCartNeededEqualToTrue() {
        val x = Mockito.mock(DelegateExecution::class.java)
        Mockito.`when`(x.getVariable(Process.Variables.SHOPPING_LIST)).thenReturn(listOf("A", "B", "C", "D", "E"))
        repeat(10) {
            CreateRandomShoppingList().execute(x)
        }
        Mockito.verify(x, Mockito.atLeast(1)).setVariable(Process.Variables.CART_NEEDED, true)
    }

    @Test
    fun testExecutionCreatesCartNeededEqualToFalse() {
        val x = Mockito.mock(DelegateExecution::class.java)
        Mockito.`when`(x.getVariable(Process.Variables.SHOPPING_LIST)).thenReturn(listOf("A", "B", "C", "D", "E"))
        repeat(10) {
            CreateRandomShoppingList().execute(x)
        }
        Mockito.verify(x, Mockito.atLeast(1)).setVariable(Process.Variables.CART_NEEDED, false)
    }
}