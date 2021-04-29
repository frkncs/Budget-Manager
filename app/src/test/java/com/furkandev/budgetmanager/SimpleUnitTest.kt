package com.furkandev.budgetmanager

import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class SimpleUnitTest {

    private var number : Int = 0
    private var multiplier : Int = 0

    @Before
    fun setup() {
        number = 5
        multiplier = 2
    }

    @Test
    fun insertAndGetSpend() {
        assertEquals(number * multiplier, 10)
    }
}