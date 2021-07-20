package de.accso.mmrsic.camundakotlindemo

import de.accso.mmrsic.camundakotlindemo.Process.Variables

/** Test case default values for [Variables] when mock implementations are used. */
val Variables.DEFAULT_VALUES: Map<String, Any>
    get() = mapOf(
        CART_NEEDED to false,
        CART_MANDATORY to false,
        SHOPPING_LIST to listOf("Bread"),
        ALL_GOODS_BOUGHT to true
    )