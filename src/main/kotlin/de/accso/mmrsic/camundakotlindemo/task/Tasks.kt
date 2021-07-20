package de.accso.mmrsic.camundakotlindemo.task

import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.delegate.JavaDelegate
import java.awt.Color
import java.io.Serializable

/** Creates a ${shoppingList} variable containing alls goods. */
interface CreateShoppingListTask : JavaDelegate
/** Creates a ${meansOfPayment} variable containing a single [MeansOfPayment].*/
interface PrepareMeansOfPaymentTask : JavaDelegate
/** Creates a ${shoppingCartDeposit} variable containing a [ShoppingCartDeposit]. */
interface PrepareShoppingCartDepositTask : JavaDelegate
/** Creates a ${shoppingCartMandatory} variable containing a boolean value. */
interface CheckShoppingCartMandatoryTask : JavaDelegate
/** Creates a ${shoppingCartTaken} variable containing a boolean flag. */
interface TakeShoppingCartTask : JavaDelegate
/** Creates variable ${shoppingCartContents} with goods found in ${shoppingList} if present in the market. */
interface ChooseGoodsTask : JavaDelegate
/** Pay the goods currently in variable ${shoppingCartContents} and set variable ${allGoodsBought} according to variable ${shoppingList}. */
interface PayGoodsTask : JavaDelegate
/** Create a new shopping list in variable ${shoppingList} consisting of all goods which weren't bought after
 * successful payment of a part of the original shopping list. */
interface CreateNewShoppingListTask : JavaDelegate

/** Abstract super-class for all shopping cart deposit variants. */
sealed class ShoppingCartDeposit(open val diameter: Double, open val thickness: Double) : Serializable

/** A chip representing a [ShoppingCartDeposit]. */
data class ShoppingCartChip(override val diameter: Double, val color: Color, override val thickness: Double) :
    ShoppingCartDeposit(diameter, thickness)

/** A coin which may be used as [ShoppingCartDeposit]. */
data class Coin(val value: Int, val unit: String, override val diameter: Double, override val thickness: Double) :
    ShoppingCartDeposit(diameter, thickness)

/** All the means of payment supported by the shopping process. */
enum class MeansOfPayment { CASH, DEBIT_CARD, CREDIT_CARD }

/** A list set as variable with a given [name] in this [DelegateExecution] instance
 * or the [emptyList] if not present. */
internal fun <T> DelegateExecution.getMandatoryList(name: String): List<T> {
    return if (hasVariable(name)) getVariable(name) as List<T> else emptyList()
}

/** Set a variable with a given [name] only if it is not present in this [DelegateExecution] instance. */
internal fun DelegateExecution.setVariableIfMissing(name: String, newValueLambda: () -> Any) {
    if (!hasVariable(name)) setVariable(name, newValueLambda())
}