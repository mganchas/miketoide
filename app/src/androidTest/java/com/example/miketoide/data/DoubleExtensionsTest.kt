package com.example.miketoide.data

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.miketoide.data.extensions.roundToOneDecimal
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DoubleExtensionsTest
{
    @Test
    fun roundToOneDecimal_receivesOneDecimal_returnsOneLengthDecimal() {
        val dummyDouble = 0.0
        val afterValue = dummyDouble.roundToOneDecimal().toString()
        val decimals = afterValue.substring(afterValue.indexOf('.') + 1)
        Assert.assertTrue(decimals.length == 1)
    }

    @Test
    fun roundToOneDecimal_receivesTwoDecimals_returnsOneLengthDecimal() {
        val dummyDouble = 0.51
        val afterValue = dummyDouble.roundToOneDecimal().toString()
        val decimals = afterValue.substring(afterValue.indexOf('.') + 1)
        Assert.assertTrue(decimals.length == 1)
    }

    @Test
    fun roundToOneDecimal_receivesThreeDecimals_returnsOneLengthDecimal() {
        val dummyDouble = 0.514
        val afterValue = dummyDouble.roundToOneDecimal().toString()
        val decimals = afterValue.substring(afterValue.indexOf('.') + 1)
        Assert.assertTrue(decimals.length == 1)
    }
}