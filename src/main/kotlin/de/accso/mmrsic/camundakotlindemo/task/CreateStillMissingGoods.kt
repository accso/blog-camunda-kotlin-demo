package de.accso.mmrsic.camundakotlindemo.task

import de.accso.mmrsic.camundakotlindemo.Process
import de.accso.mmrsic.camundakotlindemo.logger
import org.camunda.bpm.engine.delegate.DelegateExecution
import javax.inject.Named

/** [CreateNewShoppingListTask] calculating the still missing goods from the values of
 *  [Process.Variables.SHOPPING_LIST] and [Process.Variables.GOODS]. */
@Named("createNewShoppingListTask")
class CreateStillMissingGoods : CreateNewShoppingListTask {

    override fun execute(execution: DelegateExecution) {
        with(Process.Variables) {
            val shoppingList = execution.getMandatoryList<String>(SHOPPING_LIST)
            val goodsBought = execution.getMandatoryList<String>(GOODS)
            val stillMissingGoods = shoppingList.minus(goodsBought)
            execution.setVariable(SHOPPING_LIST, stillMissingGoods)
            logger.info("Still missing goods: $stillMissingGoods")
        }
    }
}