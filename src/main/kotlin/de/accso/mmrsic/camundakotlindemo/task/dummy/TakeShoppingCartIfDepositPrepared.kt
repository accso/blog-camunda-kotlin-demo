package de.accso.mmrsic.camundakotlindemo.task.dummy

import de.accso.mmrsic.camundakotlindemo.Process
import de.accso.mmrsic.camundakotlindemo.logger
import de.accso.mmrsic.camundakotlindemo.task.TakeShoppingCartTask
import org.camunda.bpm.engine.delegate.BpmnError
import org.camunda.bpm.engine.delegate.DelegateExecution
import javax.inject.Named

/** A dummy [TakeShoppingCartTask] setting [Process.Variables.CART_TAKEN] with true if and only if it's value is
 * currently not set and throwing an error otherwise. */
@Named("takeShoppingCartTask")
class TakeShoppingCartIfDepositPrepared : TakeShoppingCartTask {

    override fun execute(execution: DelegateExecution) {
        with(Process.Variables) {
            if (!execution.hasVariable(CART_DEPOSIT)) throw BpmnError("Missing deposit for shopping cart")
            execution.setVariable(CART_TAKEN, true)
            logger.info("Shopping cart taken")
        }
    }
}