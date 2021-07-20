package de.accso.mmrsic.camundakotlindemo

/** Process constants. */
object Process {
    const val NAME = "Shopping"
    const val FILE_NAME = "shopping.bpmn"

    /** Activity IDs of [Process]. */
    object ActivityIds {
        const val CREATE_SHOPPING_LIST = "CreateShoppingList"
        const val PREPARE_MEANS_OF_PAYMENT = "PrepareMeansOfPayment"
        const val PREPARE_CART_DEPOSIT = "PrepareShoppingCartDeposit"
        const val PREPARE_SHOPPING_COMPLETED = "PrepareShoppingCompleted"
        const val TAKE_CART = "TakeShoppingCart"
        const val CHOOSE_GOODS = "ChooseGoods#multiInstanceBody"
        const val PAY_GOODS = "PayGoods"
        const val CREATE_NEW_SHOPPING_LIST = "CreateNewShoppingList"
        const val SHOPPING_COMPLETED = "PerformShoppingCompleted"
        const val COMPLETED = "ShoppingCompleted"
        const val PREPARATION_FAILED = "ShoppingPreparationFailure"
        const val SHOPPING_FAILED = "ShoppingFailed"
    }

    enum class Variable(val camundaName: String) {
        SHOPPING_LIST("shoppingList"),
        CART_NEEDED("shoppingCartNeeded"),
        PAYMENT_OPTIONS("meansOfPaymentOptions"),
        CART_DEPOSIT("shoppingCartDeposit"),
        CART_MANDATORY("shoppingCartMandatory"),
        CART_TAKEN("shoppingCartTaken"),
        GOODS("goods"),
        MEANS_OF_PAYMENT("meansOfPayment"),
        BILL("bill"),
        ALL_GOODS_BOUGHT("allGoodsBought")
    }

    /** Variable names of the [Process]. */
    object Variables {
        /** List of names of all goods which are to be bought. */
        const val SHOPPING_LIST = "shoppingList"
        /** Whether a shopping cart is needed in order to buy the goods of the shopping list (according to the buyer).
         * Do not confuse with [CART_MANDATORY]. */
        const val CART_NEEDED = "shoppingCartNeeded"
        /** All payment options accepted by the buyer. */
        const val PAYMENT_OPTIONS = "meansOfPaymentOptions"
        /** Optional (prepared) deposit used for the shopping cart if needed or mandatory. */
        const val CART_DEPOSIT = "shoppingCartDeposit"
        /** Whether a shopping cart is mandatory when entering the store. */
        const val CART_MANDATORY = "shoppingCartMandatory"
        /** Whether a shopping cart was taken when entering the store. */
        const val CART_TAKEN = "shoppingCartTaken"
        /** Currently carried goods. */
        const val GOODS = "goods"
        /** Actually chosen */
        const val MEANS_OF_PAYMENT = "meansOfPayment"
        /** Bill received for the goods bought. */
        const val BILL = "bill"
        /** Whether all goods on the shopping list were bought after the store was left. */
        const val ALL_GOODS_BOUGHT = "allGoodsBought"
    }
}

/** Camunda's constants used by the [Process]. */
object Camunda {
    /** Camunda's variables used by the [Process]. */
    object Variables {
        /** Name for loop counter variable used by Camunda. */
        const val LOOP_COUNTER = "loopCounter"
    }
}