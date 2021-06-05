package com.example.miketoide.data.extensions

import kotlin.math.log2
import kotlin.math.pow

fun Long.toHumanDownloadedTimesFormat() : String {
    return when(this) {
        0.toLong() -> "-"
        in 1..100 -> "< 100"
        in 100..1000 -> "100 - 1000"
        in 1001..1000000 -> "${this / 1000} m"
        else -> "${this / 1000000} M"
    }
}

fun Long.toHumanDownloadSizeFormat() : String {
    return log2(if (this != 0L) toDouble() else 1.0).toInt().div(10).let {
        val precision = when (it) {
            0 -> 0
            1 -> 1
            else -> 2
        }
        val prefix = arrayOf("", "K", "M", "G", "T", "P", "E", "Z", "Y")
        String.format("%.${precision}f ${prefix[it]}B", toDouble() / 2.0.pow(it * 10.0))
    }
}