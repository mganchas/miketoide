package com.example.miketoide.data.extensions

import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

fun Double.roundToOneDecimal() : Double {
    val symbolsUS = DecimalFormatSymbols.getInstance(Locale.US)
    val df = DecimalFormat("#.#", symbolsUS)
    df.roundingMode = RoundingMode.FLOOR
    return df.format(this).toDouble()
}