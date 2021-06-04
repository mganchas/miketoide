package com.example.miketoide.data.extensions

fun Long.toHumanDownloadFormat() : String {
    return when(this) {
        0.toLong() -> "-"
        in 1..100 -> "< 100"
        in 100..1000 -> "100 - 1000"
        in 1001..1000000 -> "${this / 1000} m"
        else -> "${this / 1000000} M"
    }
}

fun Long.toHumanSizeFormat() : String {
    return when(this) {
        0.toLong() -> "0 kb"
        // TODO ... logica mb
        else -> "dasda"
    }
}