package de.accso.mmrsic.camundakotlindemo.task.dummy

import de.accso.mmrsic.camundakotlindemo.Process
import de.accso.mmrsic.camundakotlindemo.logger
import de.accso.mmrsic.camundakotlindemo.task.MeansOfPayment
import de.accso.mmrsic.camundakotlindemo.task.PayGoodsTask
import de.accso.mmrsic.camundakotlindemo.task.getMandatoryList
import org.camunda.bpm.engine.delegate.BpmnError
import org.camunda.bpm.engine.delegate.DelegateExecution
import java.time.LocalDateTime
import javax.inject.Named
import kotlin.math.abs
import kotlin.random.Random

/** A dummy [PayGoodsTask] randomly choosing the [Process.Variables.MEANS_OF_PAYMENT] from the
 * [Process.Variables.PAYMENT_OPTIONS], setting [Process.Variables.BILL] to a random value. */
@Named("payGoodsTask")
class PayGoodsWithAnyPaymentOption : PayGoodsTask {

    override fun execute(execution: DelegateExecution) {
        val paymentOptions = execution.getMandatoryList<MeansOfPayment>(Process.Variables.PAYMENT_OPTIONS)
        logger.debug("Payment options: $paymentOptions")
        val meansOfPayment =
            if (paymentOptions.isNotEmpty()) paymentOptions.random() else throw BpmnError("No payment options available")
        execution.setVariable(Process.Variables.MEANS_OF_PAYMENT, meansOfPayment)
        val goodsToPay = execution.getMandatoryList<String>(Process.Variables.GOODS)
        logger.info("Paying goods $goodsToPay with $meansOfPayment")
        val bill = "Bill #${abs(Random.nextInt())} ${LocalDateTime.now()}"
        execution.setVariable(Process.Variables.BILL, bill)
        logger.info("Got bill: $bill")

        val shoppingList = execution.getMandatoryList<String>(Process.Variables.SHOPPING_LIST)
        val allGoodsBought = goodsToPay.containsAll(shoppingList)
        execution.setVariable(Process.Variables.ALL_GOODS_BOUGHT, allGoodsBought)
        logger.info("All goods bought: $allGoodsBought")
    }
}