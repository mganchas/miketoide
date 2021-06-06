package com.example.miketoide.data.extensions

import java.util.*
import kotlin.math.log2
import kotlin.math.pow

fun Long.toHumanDownloadedTimesFormat() : String {
    return when(this) {
        0.toLong() -> "-"
        in 1..100 -> "< 100"
        in 100..1000 -> "100 - 1000"
        in 1001..1000000 -> "${this / 1000} k"
        else -> "${this / 1000000} M"
    }
}

fun Long.toHumanDownloadSizeFormat() : String {
    return log2(if (this != 0L) toDouble() else 1.0).toInt().div(10).let {
        val precision = when {
            it > 1 -> 2
            else -> it
        }
        val prefix = arrayOf("", "K", "M", "G")
        String.format(Locale.US, "%.${precision}f ${prefix[it]}B", toDouble() / 2.0.pow(it * 10.0))
    }
}