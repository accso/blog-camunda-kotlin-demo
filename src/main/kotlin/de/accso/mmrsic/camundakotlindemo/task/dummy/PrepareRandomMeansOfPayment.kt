package de.accso.mmrsic.camundakotlindemo.task.dummy

import de.accso.mmrsic.camundakotlindemo.Process
import de.accso.mmrsic.camundakotlindemo.logger
import de.accso.mmrsic.camundakotlindemo.task.MeansOfPayment
import de.accso.mmrsic.camundakotlindemo.task.PrepareMeansOfPaymentTask
import de.accso.mmrsic.camundakotlindemo.task.setVariableIfMissing
import org.camunda.bpm.engine.delegate.DelegateExecution
import java.lang.Integer.min
import javax.inject.Named

/** A dummy [PrepareMeansOfPaymentTask] filling [Process.Variables.PAYMENT_OPTIONS] with an arbitrary [MeansOfPayment]
 * collection. */
@Named("prepareMeansOfPaymentTask")
class PrepareRandomMeansOfPayment : PrepareMeansOfPaymentTask {
    /** Maximum number of payment options chosen by [PrepareRandomMeansOfPayment]. */
    private val numPaymentOptions = 2

    override fun execute(execution: DelegateExecution) {
        execution.setVariableIfMissing(Process.Variables.PAYMENT_OPTIONS) {
            val allPossibleValues = MeansOfPayment.values().toList()
            allPossibleValues.shuffled().subList(0, min(allPossibleValues.size, numPaymentOptions)).toList()
                .also { logger.info("Prepared payment options: $it") }
        }
    }
}
