package de.accso.mmrsic.camundakotlindemo.task.dummy

import kotlin.random.Random

/** A random boolean according to given [oddsForTrue].
 * @param oddsForTrue a value between 0 (inclusive) and 1 (exclusive)  */
fun Random.nextBoolean(oddsForTrue: Double): Boolean = nextDouble() < oddsForTrue