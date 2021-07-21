package de.accso.mmrsic.camundakotlindemo.task.dummy

import de.accso.mmrsic.camundakotlindemo.Camunda
import de.accso.mmrsic.camundakotlindemo.Process
import de.accso.mmrsic.camundakotlindemo.logger
import de.accso.mmrsic.camundakotlindemo.task.ChooseGoodsTask
import de.accso.mmrsic.camundakotlindemo.task.getMandatoryList
import org.camunda.bpm.engine.delegate.DelegateExecution
import javax.inject.Named
import kotlin.random.Random

/** A dummy [ChooseGoodsTask] filling [Process.Variables.GOODS] randomly from [Process.Variables.SHOPPING_LIST]. */
@Named("chooseGoodsTask")
class ChooseGoodsRandomly : ChooseGoodsTask {

    override fun execute(execution: DelegateExecution) {
        val shoppingList = execution.getMandatoryList<String>(Process.Variables.SHOPPING_LIST)
        val searchItem = getItemToSearchFor(execution, shoppingList)
        val success = Random.nextBoolean(17 / 19.0)
        if (success) takeGoods(execution, searchItem) else logger.warn("Could not find item '$searchItem' in store")
    }

    /** A single value from a given [shoppingList] as defined by a given [DelegateExecution]'s
     * [Camunda.Variables.LOOP_COUNTER] value. */
    private fun getItemToSearchFor(execution: DelegateExecution, shoppingList: List<String>): String {
        // The loop counter is guaranteed to range from zero to the size of the shopping list minus 1
        // (in Kotlin: 0 until size)
        val loopCounter = execution.getVariable(Camunda.Variables.LOOP_COUNTER) as Int
        logger.debug("Iteration: ${loopCounter + 1}/${shoppingList.size}")
        if (loopCounter !in shoppingList.indices)
            error("Illegal loop counter: $loopCounter for shopping list of size ${shoppingList.size}: $shoppingList")
        return shoppingList[loopCounter]
    }

    /** Take a given [newElement] the store shelf adding it to the [Process.Variables.GOODS] currently carried by the customer. */
    private fun takeGoods(execution: DelegateExecution, newElement: String) {
        val previouslyChosenGoods = execution.getMandatoryList<String>(Process.Variables.GOODS)
        val chosenGoods = previouslyChosenGoods.toMutableList().apply { add(newElement) }
        execution.setVariable(Process.Variables.GOODS, chosenGoods)
        logger.info("Found item '$newElement' in store, carrying: $chosenGoods")
    }
}