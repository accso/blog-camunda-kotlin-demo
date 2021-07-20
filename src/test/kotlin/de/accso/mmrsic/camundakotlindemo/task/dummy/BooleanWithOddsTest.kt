package de.accso.mmrsic.camundakotlindemo.task.dummy

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.RepeatedTest
import kotlin.random.Random

internal class BooleanWithOddsTest {

    @RepeatedTest(10)
    fun nextBooleanWithNegativeOddsYieldsFalse() {
        assertFalse(Random.nextBoolean(-.01))
    }

    @RepeatedTest(10)
    fun nextBooleanWithOddsEqualToOneYieldsTrue() {
        assertTrue(Random.nextBoolean(1.0))
    }

}