package de.accso.mmrsic.camundakotlindemo.task.dummy

import de.accso.mmrsic.camundakotlindemo.Process
import de.accso.mmrsic.camundakotlindemo.logger
import de.accso.mmrsic.camundakotlindemo.task.Coin
import de.accso.mmrsic.camundakotlindemo.task.PrepareShoppingCartDepositTask
import de.accso.mmrsic.camundakotlindemo.task.ShoppingCartChip
import de.accso.mmrsic.camundakotlindemo.task.setVariableIfMissing
import org.camunda.bpm.engine.delegate.DelegateExecution
import java.awt.Color
import javax.inject.Named

/** All supported shopping cart deposit options. */
val cartDepositOptions = listOf(
    ShoppingCartChip(Color.YELLOW, diameter = 23.25, thickness = 2.0),
    ShoppingCartChip(Color.RED, diameter = 24.0, thickness = 2.21),
    Coin(1, "Euro", diameter = 23.25, thickness = 2.33),
    Coin(50, "Eurocent", diameter = 24.25, thickness = 2.38)
)

/** A dummy [PrepareShoppingCartDepositTask] filling [Process.Variables.CART_DEPOSIT] with a random value. */
@Named("prepareShoppingCartDepositTask")
class PrepareRandomShoppingCartDeposit : PrepareShoppingCartDepositTask {

    override fun execute(execution: DelegateExecution) {
        execution.setVariableIfMissing(Process.Variables.CART_DEPOSIT) {
            cartDepositOptions.random().also { deposit -> logger.info("Prepared shopping cart deposit: $deposit") }
        }
    }
}
