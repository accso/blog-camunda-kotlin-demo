package de.accso.mmrsic.camundakotlindemo.task.dummy

import de.accso.mmrsic.camundakotlindemo.Process
import de.accso.mmrsic.camundakotlindemo.logger
import de.accso.mmrsic.camundakotlindemo.task.CheckShoppingCartMandatoryTask
import de.accso.mmrsic.camundakotlindemo.task.setVariableIfMissing
import org.camunda.bpm.engine.delegate.DelegateExecution
import javax.inject.Named
import kotlin.random.Random

/** A dummy [CheckShoppingCartMandatoryTask] randomly choosing a value for [Process.Variables.CART_MANDATORY]. */
@Named("checkShoppingCartMandatoryTask")
class CheckShoppingCartRandomlyMandatory : CheckShoppingCartMandatoryTask {

    override fun execute(execution: DelegateExecution) {
        execution.setVariableIfMissing(Process.Variables.CART_MANDATORY) {
            Random.nextBoolean(0.29)
                .also { logger.info("Shopping cart mandatory because of pandemic restrictions: $it") }
        }
    }
}