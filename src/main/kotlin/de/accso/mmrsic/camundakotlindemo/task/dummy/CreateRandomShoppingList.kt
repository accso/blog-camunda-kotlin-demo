package de.accso.mmrsic.camundakotlindemo.task.dummy

import de.accso.mmrsic.camundakotlindemo.Process
import de.accso.mmrsic.camundakotlindemo.logger
import de.accso.mmrsic.camundakotlindemo.task.CreateShoppingListTask
import de.accso.mmrsic.camundakotlindemo.task.getMandatoryList
import de.accso.mmrsic.camundakotlindemo.task.setVariableIfMissing
import org.camunda.bpm.engine.delegate.DelegateExecution
import java.lang.Integer.min
import javax.inject.Named
import kotlin.random.Random

/** All goods which may show up on a shopping list. */
internal object AllGoods {
    /** The names of all goods supported by this dummy implementation. */
    val names = listOf(
        "Water", "Bread", "Tomatoes", "Milk", "Soap", "Chewing gum", "Pencil", "College block",
        "Spoon", "Fork", "Knife", "Plate", "Tablecloth", "Chair", "Table", "Frying pan", "Stock pot"
    )

    /** Randomly chosen [names] optionally with a given [resultSize]. */
    fun randomNames(resultSize: Int = names.size / 2) =
        names.toMutableList().shuffled().subList(0, min(names.size, resultSize)).toList()
}

/**
 * A dummy [CreateShoppingListTask] filling [Process.Variables.SHOPPING_LIST] with a random list and randomly deciding
 *  whether [Process.Variables.CART_NEEDED].
 */
@Named("createShoppingListTask")
class CreateRandomShoppingList : CreateShoppingListTask {
    /** Number of items in generated shopping list. */
    private val shoppingListSize = 5

    override fun execute(execution: DelegateExecution) {
        execution.setVariableIfMissing(Process.Variables.SHOPPING_LIST) {
            AllGoods.randomNames(shoppingListSize).also { logger.info("Created shopping list: $it") }
        }
        execution.setVariableIfMissing(Process.Variables.CART_NEEDED) {
            val shoppingList = execution.getMandatoryList<String>(Process.Variables.SHOPPING_LIST)
            val cartNeeded = Random.nextBoolean(shoppingListSize - 2.0 / (shoppingListSize))
            cartNeeded.also { logger.info("Shopping cart needed: $it") }
        }
    }
}